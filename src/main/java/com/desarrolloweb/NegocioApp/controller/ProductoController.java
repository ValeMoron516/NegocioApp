package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.dtos.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dtos.ProductoDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.service.ProductoService;

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
@RequestMapping("api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ##################################################

    // Leer todos los productos (paginadas)
    @GetMapping
    public ResponseEntity<PaginacionDTO<ProductoDTO>> obtenerTodosProductos(
        @RequestParam(defaultValue = "1") Integer page, 
        @RequestParam(defaultValue = "20") Integer limit) {
        return ResponseEntity.status(HttpStatus.OK)
        .body(productoService.obtenerTodosProductos(page, limit)); // 200
    }

    // ##################################################

    // Leer producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(productoService.obtenerProductoPorId(id)); // 200
        } 
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // ##################################################

    // Crear nuevo producto
    @PostMapping
    public ResponseEntity<ProductoDTO> crearPtoducto(
        @RequestBody ProductoDTO p) {
        
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
            .body(productoService.crearProducto(p)); // 201
        }
        catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
        catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }

    // ##################################################

    // Actualizar producto por id
    @PutMapping("{id}")
    public ResponseEntity<ProductoDTO> actualizarProductoPorId(@PathVariable Long id, @RequestBody ProductoDTO p) {
        
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(productoService.actualizarProductoPorId(id, p)); // 200
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
    
    // ##################################################

    // Borrar por ID
    @DeleteMapping("{id}")
    public ResponseEntity<?> borrarProductoPorId(@PathVariable Long id) {
        try {
            productoService.borrarProductoPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}
