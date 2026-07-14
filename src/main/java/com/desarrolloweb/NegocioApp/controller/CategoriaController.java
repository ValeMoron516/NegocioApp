package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.dtos.CategoriaDTO;
import com.desarrolloweb.NegocioApp.dtos.PaginacionDTO;
import com.desarrolloweb.NegocioApp.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

// ##################################################

    // Leer todas las Categorias (paginadas)
    @GetMapping
    public ResponseEntity<PaginacionDTO<CategoriaDTO>> obtenerTodasCategorias(
        @RequestParam(defaultValue = "1") Integer page, 
        @RequestParam(defaultValue = "20") Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.obtenerTodasCategorias(page, limit)); // 200
    }

// ##################################################

    // Leer Categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            CategoriaDTO cDto = categoriaService.obtenerCategoriaPorId(id);
            // Existe
            return ResponseEntity.status(HttpStatus.OK).body(cDto); // 200
        } catch (NoSuchElementException e) {
            // No existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

// ##################################################

    // Crear nueva categoria
    @PostMapping
    public ResponseEntity<CategoriaDTO> crearCategoria(
        @RequestParam String nombre, 
        @RequestParam String descripcion) {
        
        try {
            CategoriaDTO cDto = categoriaService.crearCategoria(nombre, descripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(cDto); // 201
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
        catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }

// ##################################################

    // Actualizar por id
    //@PutMapping("{id}")
    //public void actualizarCategoriaPorId(@PathVariable Long id, @RequestBody Categoria categoria) {
    //    categoriaService.actualizarCategoriaPorId(id, categoria);
    //}
    
    // Borrar por ID
    //@DeleteMapping("{id}")
    //public void borrarCategoriaPorId(@PathVariable Long id) {
    //    categoriaService.borrarCategoriaPorId(id);
    //}
}
