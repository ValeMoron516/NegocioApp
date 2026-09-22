package com.desarrolloweb.NegocioApp.productoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.desarrolloweb.NegocioApp.controller.ProductoController;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoResponseDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.service.ProductoService;
import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {
    
    @Mock
    ProductoService productoService; // Modulo simulado

    @InjectMocks
    ProductoController productoController; // Modulo principal

    // ##################################################

    @Test
    void obtenerTodosProductos_200() {
        Integer page = 1;
        Integer limit = 20;
        MetaDTO metaVacio = new MetaDTO(0L, 0, 20, 0, 1);
        PaginacionDTO<ProductoResponseDTO> respuestaService = new PaginacionDTO<>(new ArrayList<>(), metaVacio);
        when(productoService.obtenerTodosProductos(page, limit)).thenReturn(respuestaService);

        ResponseEntity<PaginacionDTO<ProductoResponseDTO>> respuestaHTTP = productoController.obtenerTodosProductos(page, limit);

        assertEquals(HttpStatus.OK, respuestaHTTP.getStatusCode());
        assertEquals(respuestaService, respuestaHTTP.getBody());
    }

    // ##################################################

    @Test
    void obtenerProductoPorId_200() {
        Long id = 1L;
        ProductoResponseDTO respuestaService = new ProductoResponseDTO(
            id, 1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        when(productoService.obtenerProductoPorId(id)).thenReturn(respuestaService);

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = productoController.obtenerProductoPorId(id);

        assertEquals(HttpStatus.OK, respuestaHTTP.getStatusCode());
        assertEquals(respuestaService, respuestaHTTP.getBody());
    }

    @Test
    void obtenerProductoPorId_404() {
        Long id = 1L;
        when(productoService.obtenerProductoPorId(id)).thenThrow(new NotFoundException());

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = productoController.obtenerProductoPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, respuestaHTTP.getStatusCode());
    }

    // ##################################################

    @Test
    void crearProducto_201() {
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        ProductoResponseDTO respuestaService = new ProductoResponseDTO(
            1L, 1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        when(productoService.crearProducto(peticion)).thenReturn(respuestaService);

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = productoController.crearProducto(peticion);

        assertEquals(HttpStatus.CREATED, respuestaHTTP.getStatusCode());
        assertEquals(1L, respuestaHTTP.getBody().getId());
    }

    @Test
    void crearProducto_400() {
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        when(productoService.crearProducto(peticion)).thenThrow(new BadRequestException());

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = productoController.crearCategoria(peticion);

        assertEquals(HttpStatus.BAD_REQUEST, respuestaHTTP.getStatusCode());
    }

    @Test
    void crearProducto_409() {
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        when(productoService.crearProducto(peticion)).thenThrow(new ConflictException());

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = categoriaController.crearCategoria(peticion);

        assertEquals(HttpStatus.CONFLICT, respuestaHTTP.getStatusCode());
    }

    // ##################################################

    @Test
    void actualizarProductoPorId_200() {
        Long id = 1L;
        ProductoRequestDTO peticion = new CategoriaRequestDTO(
            1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        ProductoResponseDTO respuesta = new ProductoResponseDTO(
            id, 1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        when(productoService.actualizarProductoPorId(id, peticion)).thenReturn(respuesta);

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = productoController.actualizarProductoPorId(id, peticion);

        assertEquals(HttpStatus.OK, respuestaHTTP.getStatusCode());
    }

    @Test
    void actualizarProductoPorId_404() {
        Long id = 1L;
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        when(productoService.actualizarProductoPorId(id, peticion)).thenThrow(new NotFoundException());

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = productoController.actualizarProductoPorId(id, peticion);

        assertEquals(HttpStatus.NOT_FOUND, respuestaHTTP.getStatusCode());
    }

    @Test
    void actualizarProductoPorId_409() {
        Long id = 1L;
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio Pablito", 1L, "Electronica", "Monitor 24 pulgadas", "Monitor de 24 pulgadas 1920x1080 OLED", 149999, 10
        );
        when(productoService.actualizarProductoPorId(id, peticion)).thenThrow(new ConflictException());

        ResponseEntity<ProductoResponseDTO> respuestaHTTP = productoController.actualizarProductoPorId(id, peticion);

        assertEquals(HttpStatus.CONFLICT, respuestaHTTP.getStatusCode());
    }

    // ##################################################

    @Test
    void borrarProducto_204() {
        Long id = 1L;

        ResponseEntity<Void> respuestaHTTP = productoController.borrarProductoPorId(id);

        assertEquals(HttpStatus.NO_CONTENT, respuestaHTTP.getStatusCode());
    }

    @Test
    void borrarProducto_400() {
        Long id = 1L;
        doThrow(new NotFoundException()).when(productoService).borrarCategoriaPorId(id);

        ResponseEntity<Void> respuestaHTTP = productoController.borrarProductoPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, respuestaHTTP.getStatusCode());
    }

    @Test
    void borrarProducto_409() {
        Long id = 1L;
        doThrow(new ConflictException()).when(productoService).borrarProductoPorId(id);

        ResponseEntity<Void> respuestaHTTP = productoController.borrarProductoPorId(id);

        assertEquals(HttpStatus.CONFLICT, respuestaHTTP.getStatusCode());
    }

}
