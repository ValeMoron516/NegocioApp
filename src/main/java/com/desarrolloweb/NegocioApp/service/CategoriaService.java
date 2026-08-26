package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Categoria;

import com.desarrolloweb.NegocioApp.dtos.CategoriaDTO;
import com.desarrolloweb.NegocioApp.dtos.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.PaginacionDTO;

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
    public PaginacionDTO<CategoriaDTO> obtenerTodasCategorias(Integer page, Integer limit) { 
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Categoria> paginaCategorias = categoriaRepository.findAll(pageable);
        List<Categoria> categorias = paginaCategorias.getContent();
        
        // Mapear a DTO
        List<CategoriaDTO> dtos = new ArrayList<>();
        for (Categoria c : categorias) {
            CategoriaDTO dto = new CategoriaDTO();
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
    public CategoriaDTO obtenerCategoriaPorId(Long id) { 
        Optional<Categoria> optC = categoriaRepository.findById(id);
        
        // Existe
        if (optC.isPresent()) {
            Categoria c = optC.get();
            
            return new CategoriaDTO(
                c.getId(), 
                c.getNombre(), 
                c.getDescripcion());
        }
        
        // No existe
        throw new NotFoundException("El elemento solicitado no existe");
    }

    // ##################################################

    // Crear nueva Categoria
    public CategoriaDTO crearCategoria(CategoriaDTO cDTO) {

        Categoria c = new Categoria();

        // Verificar nombre
        if (cDTO.getNombre() == null || cDTO.getNombre().isEmpty()) { throw new BadRequestException("Nombre invalido"); }
        c.setNombre(cDTO.getNombre());
        // Verificar descripcion
        if (cDTO.getDescripcion() == null || cDTO.getDescripcion().isEmpty()) { throw new BadRequestException("Descripcion invalida"); }
        c.setDescripcion(cDTO.getDescripcion());

        // =========================================

        Optional<Categoria> optC = categoriaRepository.findByNombre(cDTO.getNombre());
        
        if (optC.isPresent()) { // Ya existe
            throw new ConflictException("Ya existe una categoria con el nombre provisto"); } 
        else { // No existe
            Categoria cResp = categoriaRepository.save(c);
            return new CategoriaDTO(
                cResp.getId(), 
                cResp.getNombre(), 
                cResp.getDescripcion()); // Exito
        }
    }

    // ##################################################

    // Actualizar Categoria por ID
    public CategoriaDTO actualizarCategoriaPorId(Long id, CategoriaDTO cDTO) {
        Optional<Categoria> optC = categoriaRepository.findById(id);

        if (optC.isPresent()) {
            Categoria c = optC.get();
            
            // Verificar nombre
            if (cDTO.getNombre() != null || !cDTO.getNombre().isBlank()) {
                Optional<Categoria> optCNombre = categoriaRepository.findByNombre(cDTO.getNombre());
                
                if (optCNombre.isPresent()) { throw new ConflictException("El nombre provisto ya existe"); }
                c.setNombre(cDTO.getNombre());
            }
            
            // Verificar descripcion
            if (cDTO.getDescripcion() != null || !cDTO.getDescripcion().isBlank()) {
                c.setDescripcion(cDTO.getDescripcion());
            }
            // =========================================
            
            // Guardar nuevo
            Categoria cResp = categoriaRepository.save(c);
            return new CategoriaDTO(
                cResp.getId(), 
                cResp.getNombre(), 
                cResp.getDescripcion());
        }
        else {
            throw new NotFoundException("La categoria con el ID provisto no existe");
        }
    }

    // ##################################################

    // Borrar Categoria por id
    public void borrarCategoriaPorId(Long id) {
         
         Optional<Categoria> optC = categoriaRepository.findById(id);

        if (optC.isPresent()) { // Existe
            
            // Buscar productos asociados a la id
            if (productoRepository.existsByCategoriaId(id)) { // Hay productos
                throw new ConflictException("La categoria provista no puede eliminarse debido a que tiene productos asociados");
            }
            
            categoriaRepository.deleteById(id);
            
        } else { // No existe
            throw new NotFoundException("La categoria con el ID provisto no existe");
        }
    }
}
