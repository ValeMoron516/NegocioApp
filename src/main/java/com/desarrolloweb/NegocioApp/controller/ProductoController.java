package com.desarrolloweb.NegocioApp.controller;
 
import com.desarrolloweb.NegocioApp.dto.paginacion.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dto.producto.ProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dto.producto.ProductoResponseDTO;
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
    @org.springframework.beans.factory.annotation.Autowired
    private com.desarrolloweb.NegocioApp.service.AccessGuard guard;


    @Autowired
    private ProductoService productoService;

    // ##################################################

    // Leer todos los productos (paginadas)
    @GetMapping
    public ResponseEntity<PaginacionDTO<ProductoResponseDTO>> obtenerTodosProductos(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(name = "texto", required = false) String texto,
            @RequestParam(name = "categoria", required = false) Long categoria,
            @RequestParam(name = "precio_min", required = false) java.math.BigDecimal precioMin,
            @RequestParam(name = "precio_max", required = false) java.math.BigDecimal precioMax) {
        return ResponseEntity.ok(productoService.obtenerTodosProductos(
                page, limit, texto, categoria, precioMin, precioMax));
    }

    // ##################################################

    // Leer producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(@PathVariable Long id) {
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
    public ResponseEntity<ProductoResponseDTO> crearProducto(
        @RequestBody ProductoRequestDTO p) {
        guard.createsProduct(p);

        
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
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // ##################################################

    // Actualizar producto por id
    @PutMapping("{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProductoPorId(@PathVariable Long id, @RequestBody ProductoRequestDTO p) {
        guard.updatesProduct(id,p);

        
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(productoService.actualizarProductoPorId(id, p)); // 200
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }
    
    // ##################################################

    // Borrar por ID
    @DeleteMapping("{id}")
    public ResponseEntity<Void> borrarProductoPorId(@PathVariable Long id) {
        guard.ownsProduct(id);

        try {
            productoService.borrarProductoPorId(id);
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
