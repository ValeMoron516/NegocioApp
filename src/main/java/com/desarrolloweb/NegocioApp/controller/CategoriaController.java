package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Leer todos
    @GetMapping
    public List<Categoria> obtenerTodasCategorias() {
        return categoriaService.obtenerTodasCategorias();
    }

    // Leer por ID
    @GetMapping("/{id}")
    public Optional<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.obtenerCategoriaPorId(id);
    }

    // Crear
    @PostMapping("/create")
    public void crearCategoria(@RequestBody Categoria categoria) {
        categoriaService.crearCategoria(categoria);
    }

    // Actualizar por id
    @PutMapping("/update/{id}")
    public void actualizarCategoriaPorId(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoriaService.actualizarCategoriaPorId(id, categoria);
    }
    
    // Borrar por ID
    @DeleteMapping("/delete/{id}")
    public void borrarCategoriaPorId(@PathVariable Long id) {
        categoriaService.borrarCategoriaPorId(id);
    }
}
