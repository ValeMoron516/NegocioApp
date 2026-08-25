package com.desarrolloweb.NegocioApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.service.ProductoService;

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
        .body(categoriaService.obtenerTodosProductos(page, limit)); // 200
    }

    // ##################################################

    // Leer producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(categoriaService.obtenerPtoductoPorId(id)); // 200
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
            .body(categoriaService.crearProducto(p)); // 201
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
            .body(categoriaService.actualizarProductoPorId(id, p)); // 200
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
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
