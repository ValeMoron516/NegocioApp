package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.dtos.CategoriaDTO;
import com.desarrolloweb.NegocioApp.dtos.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.PaginacionDTO;
import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;

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

// ##################################################

    // Obtener lista de categorías (paginadas)
    public PaginacionDTO<CategoriaDTO> obtenerTodasCategorias(Integer page, Integer limit) { 
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Categoria> paginaCategorias = categoriaRepository.findAll(pageable);
        List<Categoria> categorias = paginaCategorias.getContent();
        
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
            CategoriaDTO dto = new CategoriaDTO();
            
            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            
            return dto;
        }
        
        // No existe
        throw new NotFoundException("El elemento solicitado no existe");
    }

// ##################################################

    // Crear nueva Categoria
    public CategoriaDTO crearCategoria(Categoria c) {

        // nombre nulo o vacio
        if (c.nombre == null || c.nombre.isEmpty()) { throw new BadRequestException("Nombre invalido"); }
        
        // descripcion nulo o vacio
        if (c.descripcion == null || c.descripcion.isEmpty()) { throw new BadRequestException("Descripcion invalida"); }
        
        Optional<Categoria> optC = categoriaRepository.findByNombre(c.nombre);
        
        if (optC.isPresent()) { throw new ConflictException("Ya existe una categoria von el nombre provisto"); } // Ya existe
        else { // No existe
            Categoria cResp = categoriaRepository.save(c);
            return new CategoriaDTO(cResp.getId(), cResp.getNombre()); // Exito
        }
    }

// ##################################################

    // Actualizar Categoria
    public categoriaDTO actualizarCategoriaPorId(Long id, Categoria c) {
        Optional<Categoria> optC = categoriaRepository.findById(id);

        if (optC.isPresent()) {
            Categoria newC = optC.get();
            if (c.getNombre() != null || !c.getNombre().isBlank()) {
                Optional<Categoria> optC = categoriaRepository.findByNombre(c.nombre);
                if (optC.isPresent()) {
                    newC.setNombre(c.getNombre());
                } else {
                    throw new ConflictException("El nombre provisto ya existe")
                }
            }
            if (c.getDescripcion() != null || !c.getDescripcion().isBlank()) {
                newC.setDescripcion(c.getDescripcion());
            }
        
            categoriaRepository.save(newC);
            Categoria c = categoriaRepository.findById(newC.getId()).get();
            return new categoriaDTO(c.getId(), c.getNombre(), c.getDescripcion());
        }
        else {
            throw new NotFoundException("La categoria con el ID provisto no existe");  
   }

// ##################################################

    // Borrar Categoria por id
    public void borrarCategoriaPorId(Long id) {
         
         Optional<Categoria> optC = categoriaRepository.findById(id);

        if (optC.isPresent()) {
            categoriaRepository.deleteById(id);
        } else {
            throw new NotFoundException("La categoria con el ID provisto no existe");
    }
}
