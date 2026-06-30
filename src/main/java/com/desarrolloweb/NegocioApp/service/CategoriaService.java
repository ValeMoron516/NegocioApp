package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Leer todos las Categoria
    public List<Categoria> obtenerTodasCategorias() { 
        return categoriaRepository.findAll(); 
    }

    // Leer Categoria por id
    public Optional<Categoria> obtenerCategoriaPorId(Long id) { 
        return categoriaRepository.findById(id); 
    }

    // Crear nueva Categoria
    public void crearCategoria(Categoria categoria) {
        categoriaRepository.save(categoria); 
    }

    // Actualizar Categoria
    public void actualizarCategoriaPorId(Long id, Categoria categoria) {
        Categoria newRegistro = categoriaRepository.findById(id).get();

        if (categoria.getNombre() != null && !categoria.getNombre().isBlank()) {
            newRegistro.setNombre(categoria.getNombre());
        }
        if (categoria.getDescripcion() != null && !categoria.getDescripcion().isBlank()) {
            newRegistro.setDescripcion(categoria.getDescripcion());
        }
        
        categoriaRepository.save(newRegistro); 
    }

    // Borrar Categoria por id
    public void borrarCategoriaPorId(Long id) { 
        categoriaRepository.deleteById(id); 
    }
}
