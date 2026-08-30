package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.dtos.categoriaDTO.CategoriaRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.categoriaDTO.CategoriaResponseDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;

import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;
    // ##################################################

    // Obtener lista de categorías (paginadas)
    public PaginacionDTO<CategoriaResponseDTO> obtenerTodasCategorias(Integer page, Integer limit) { 
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Categoria> paginaCategorias = categoriaRepository.findAll(pageable);
        List<Categoria> categorias = paginaCategorias.getContent();
        
        // Mapear a DTO
        List<CategoriaResponseDTO> dtos = new ArrayList<>();
        for (Categoria c : categorias) {
            CategoriaResponseDTO dto = new CategoriaResponseDTO();
            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            dto.setDescripcion(c.getDescripcion());
            
            dtos.add(dto);
        }
        
        MetaDTO meta = new MetaDTO(
            paginaCategorias.getTotalElements(),     // totalItems   (elementos totales)
            paginaCategorias.getNumberOfElements(),  // itemCount    (elementos en la página actual)
            paginaCategorias.getSize(),              // itemsPerPage (elementos maximos por pagina)
            paginaCategorias.getTotalPages(),        // totalPages   (total de paginas)
            paginaCategorias.getNumber() + 1         // currentPage  (pagina actual)
        );
    
        return new PaginacionDTO<>(dtos, meta);
    }

    // ##################################################

    // Obtener Categoria por ID
    public CategoriaResponseDTO obtenerCategoriaPorId(Long id) { 

        Optional<Categoria> optC = categoriaRepository.findById(id);

        // Verificar si existe el elemento
        if (!optC.isPresent()) { throw new NotFoundException("El elemento solicitado no existe"); }
        
        Categoria c = optC.get();
        return new CategoriaResponseDTO(
            c.getId(), 
            c.getNombre(), 
            c.getDescripcion());
    }

    // ##################################################

    // Crear nueva Categoria
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO cDTO) {

        // Verificar nombre
        if (cDTO.getNombre() == null || cDTO.getNombre().isEmpty()) { 
            throw new BadRequestException("Nombre invalido"); 
        }
        
        // Verificar nombre existente
        if (categoriaRepository.existsByNombre(cDTO.getNombre())) { 
            throw new ConflictException("Ya existe una categoria con el nombre provisto"); 
        } 
        
        // Verificar descripcion
        if (cDTO.getDescripcion() == null || cDTO.getDescripcion().isEmpty()) { 
            throw new BadRequestException("Descripcion invalida"); 
        }
        
        Categoria newC = new Categoria();
        newC.setNombre(cDTO.getNombre());
        newC.setDescripcion(cDTO.getDescripcion());
        
        // =========================================

        Categoria c = categoriaRepository.save(newC);
        return new CategoriaResponseDTO(
            c.getId(), 
            c.getNombre(), 
            c.getDescripcion());
    }

    // ##################################################

    // Actualizar Categoria por ID
    public CategoriaResponseDTO actualizarCategoriaPorId(Long id, CategoriaRequestDTO cDTO) {

        // Verificar si existe la categoria
        Optional<Categoria> optC = categoriaRepository.findById(id);
        if (!optC.isPresent()) { throw new NotFoundException("La categoria con el ID provisto no existe"); }

        Categoria newC = optC.get();
        
        // Verificar nombre
        if (cDTO.getNombre() != null || !cDTO.getNombre().isBlank()) {
            // Verificar si ya existe una categoria con el nombre dado
            if (categoriaRepository.existsByNombre(cDTO.getNombre())) { 
                throw new ConflictException("El nombre provisto ya existe");
            }
            newC.setNombre(cDTO.getNombre());
        }
        
        // Verificar descripcion
        if (cDTO.getDescripcion() != null || !cDTO.getDescripcion().isBlank()) {
            newC.setDescripcion(cDTO.getDescripcion());
        }
        // =========================================
        
        Categoria c = categoriaRepository.save(newC);
        return new CategoriaResponseDTO(
            c.getId(), 
            c.getNombre(), 
            c.getDescripcion());
    }

    // ##################################################

    // Borrar Categoria por id
    public void borrarCategoriaPorId(Long id) {
         
        if (!categoriaRepository.existsById(id)) { 
            throw new NotFoundException("La categoria con el ID provisto no existe"); 
        }
        
        // Buscar productos asociados a la id
        if (productoRepository.existsByCategoriaId(id)) { // Hay productos
            throw new ConflictException("La categoria provista no puede eliminarse debido a que tiene productos asociados");
        }
        
        categoriaRepository.deleteById(id);            
    }
}
