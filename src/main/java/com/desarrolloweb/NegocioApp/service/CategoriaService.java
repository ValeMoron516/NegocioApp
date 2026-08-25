package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.dtos.CategoriaDTO;
import com.desarrolloweb.NegocioApp.dtos.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.PaginacionDTO;
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
    public CategoriaDTO crearCategoria(CategoriaDTO c) {

        // Verificar nombre
        if (c.nombre == null || c.nombre.isEmpty()) { throw new BadRequestException("Nombre invalido"); }
        // Verificar descripcion
        if (c.descripcion == null || c.descripcion.isEmpty()) { throw new BadRequestException("Descripcion invalida"); }
        
        // =========================================
        Optional<Categoria> optC = categoriaRepository.findByNombre(c.nombre);
        
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
    public CategoriaDTO actualizarCategoriaPorId(Long id, CategoriaDTO c) {
        Optional<Categoria> optC = categoriaRepository.findById(id);

        if (optC.isPresent()) {
            Categoria newC = optC.get();
            
            // Verificar nombre
            if (c.getNombre() != null || !c.getNombre().isBlank()) {
                Optional<Categoria> optC = categoriaRepository.findByNombre(c.nombre);
                
                if (optC.isPresent()) { newC.setNombre(c.getNombre()); } 
                else { throw new ConflictException("El nombre provisto ya existe"); }
            }
            
            // Verificar descripcion
            if (c.getDescripcion() != null || !c.getDescripcion().isBlank()) {
                newC.setDescripcion(c.getDescripcion());
            }
            // =========================================
            
            // Guardar nuevo
            Categoria c = categoriaRepository.save(newC);
            return new categoriaDTO(
            c.getId(), 
            c.getNombre(), 
            c.getDescripcion());
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
