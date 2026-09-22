package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Categoria;
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

    // Leer todas las Categorias (paginadas)
    public PaginacionDTO<CategoriaDTO> obtenerTodasCategorias(Integer page, Integer limit) { 
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Categoria> paginaCategorias = categoriaRepository.findAll(pageable);
        List<Categoria> categorias = paginaCategorias.getContent();
        
        List<CategoriaDTO> dtos = new ArrayList<>();
        for (Categoria c : categorias) {
            CategoriaDTO dto = new CategoriaDTO();
            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            
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

    // Leer Categoria por ID
    public Optional<CategoriaDTO> obtenerCategoriaPorId(Long id) { 
        Optional<Categoria> optC = categoriaRepository.findById(id);
        
        if (optC.isPresent()) {
            Categoria c = optC.get();
            CategoriaDTO dto = new CategoriaDTO();
            
            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            
            return Optional.of(dto);
        }
        
        return Optional.empty();
    }

    // Crear nueva Categoria
    //public void crearCategoria(Categoria categoria) {
    //    categoriaRepository.save(categoria); 
    //}

    // Actualizar Categoria
    //public void actualizarCategoriaPorId(Long id, Categoria categoria) {
    //    Categoria newRegistro = categoriaRepository.findById(id).get();

    //    if (categoria.getNombre() != null && !categoria.getNombre().isBlank()) {
    //        newRegistro.setNombre(categoria.getNombre());
    //    }
    //    if (categoria.getDescripcion() != null && !categoria.getDescripcion().isBlank()) {
    //        newRegistro.setDescripcion(categoria.getDescripcion());
    //    }
        
    //    categoriaRepository.save(newRegistro); 
    //}

    // Borrar Categoria por id
    //public void borrarCategoriaPorId(Long id) { 
    //    categoriaRepository.deleteById(id); 
    //}
}
