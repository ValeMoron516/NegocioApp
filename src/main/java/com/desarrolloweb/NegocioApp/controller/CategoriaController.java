package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
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
        return ResponseEntity.status(HttpStatus.OK)
        .body(categoriaService.obtenerTodasCategorias(page, limit)); // 200
    }

    // ##################################################

    // Leer Categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(categoriaService.obtenerCategoriaPorId(id)); // 200
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // ##################################################

    // Crear nueva categoria
    @PostMapping
    public ResponseEntity<CategoriaDTO> crearCategoria(
        @RequestBody CategoriaDTO c) {
        
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoriaService.crearCategoria(c)); // 201
        }
        catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
        catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }

    // ##################################################

    // Actualizar por id
    @PutMapping("{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoriaPorId(@PathVariable Long id, @RequestBody CategoriaDTO c) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(categoriaService.actualizarCategoriaPorId(id, c)); // 200
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }
    
    // ##################################################

    // Borrar por ID
    @DeleteMapping("{id}")
    public ResponseEntity<?> borrarCategoriaPorId(@PathVariable Long id) {
        try {
            categoriaService.borrarCategoriaPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }
}
