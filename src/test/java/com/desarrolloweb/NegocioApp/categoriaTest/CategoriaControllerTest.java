package com.desarrolloweb.NegocioApp.categoriaTest;

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

import com.desarrolloweb.NegocioApp.controller.CategoriaController;
import com.desarrolloweb.NegocioApp.dtos.categoriaDTO.CategoriaRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.categoriaDTO.CategoriaResponseDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {
    
    @Mock
    CategoriaService categoriaService; // Falsificamos la lógica de negocio

    @InjectMocks
    CategoriaController categoriaController; // Nuestro protagonista

    // ##################################################

    @Test
    void obtenerTodasCategorias_200() {
        Integer page = 1;
        Integer limit = 20;
        MetaDTO metaVacio = new MetaDTO(0L, 0, 20, 0, 1);
        PaginacionDTO<CategoriaResponseDTO> respuestaService = new PaginacionDTO<>(new ArrayList<>(), metaVacio);
        when(categoriaService.obtenerTodasCategorias(page, limit)).thenReturn(respuestaService);

        ResponseEntity<PaginacionDTO<CategoriaResponseDTO>> respuestaHTTP = categoriaController.obtenerTodasCategorias(page, limit);

        assertEquals(HttpStatus.OK, respuestaHTTP.getStatusCode());
        assertEquals(respuestaService, respuestaHTTP.getBody());
    }

    // ##################################################

    @Test
    void obtenerCategoriaPorId_200() {
        Long id = 1L;
        CategoriaResponseDTO respuestaService = new CategoriaResponseDTO(
            1L, "Hogar", "Productos para el hogar"
        );
        when(categoriaService.obtenerCategoriaPorId(id)).thenReturn(respuestaService);

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.obtenerCategoriaPorId(id);

        assertEquals(HttpStatus.OK, respuestaHTTP.getStatusCode());
        assertEquals(respuestaService, respuestaHTTP.getBody());
    }

    @Test
    void obtenerCategoriaPorId_404() {
        Long id = 1L;
        when(categoriaService.obtenerCategoriaPorId(id)).thenThrow(new NotFoundException());

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.obtenerCategoriaPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, respuestaHTTP.getStatusCode());
    }

    // ##################################################

    @Test
    void crearCategoria_201() {
        CategoriaRequestDTO peticion = new CategoriaRequestDTO(
            "Computacion", "Productos sobre computacion"
        );
        CategoriaResponseDTO respuestaService = new CategoriaResponseDTO(
            1L, "Computacion", "Productos sobre computacion"
        );
        when(categoriaService.crearCategoria(peticion)).thenReturn(respuestaService);

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.crearCategoria(peticion);

        assertEquals(HttpStatus.CREATED, respuestaHTTP.getStatusCode());
        assertEquals(1L, respuestaHTTP.getBody().getId());
    }

    @Test
    void crearCategoria_400() {
        CategoriaRequestDTO peticion = new CategoriaRequestDTO(
            "Computacion", "Productos sobre computacion"
        );
        when(categoriaService.crearCategoria(peticion)).thenThrow(new BadRequestException());

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.crearCategoria(peticion);

        assertEquals(HttpStatus.BAD_REQUEST, respuestaHTTP.getStatusCode());
    }

    @Test
    void crearCategoria_409() {
        CategoriaRequestDTO peticion = new CategoriaRequestDTO(
            "Computacion", "Productos sobre computacion"
        );
        when(categoriaService.crearCategoria(peticion)).thenThrow(new ConflictException());

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.crearCategoria(peticion);

        assertEquals(HttpStatus.CONFLICT, respuestaHTTP.getStatusCode());
    }

    // ##################################################

    @Test
    void actualizarCategoriaPorId_200() {
        Long id = 1L;
        CategoriaRequestDTO peticion = new CategoriaRequestDTO(
            "Computacion", "Productos sobre computacion"
        );
        CategoriaResponseDTO respuesta = new CategoriaResponseDTO(
            id, "Computacion", "Productos sobre computacion"
        );
        when(categoriaService.actualizarCategoriaPorId(id, peticion)).thenReturn(respuesta);

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.actualizarCategoriaPorId(id, peticion);

        assertEquals(HttpStatus.OK, respuestaHTTP.getStatusCode());
    }

    @Test
    void actualizarCategoriaPorId_404() {
        Long id = 1L;
        CategoriaRequestDTO peticion = new CategoriaRequestDTO(
            "Computacion", "Productos sobre computacion"
        );
        when(categoriaService.actualizarCategoriaPorId(id, peticion)).thenThrow(new NotFoundException());

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.actualizarCategoriaPorId(id, peticion);

        assertEquals(HttpStatus.NOT_FOUND, respuestaHTTP.getStatusCode());
    }

    @Test
    void actualizarCategoriaPorId_409() {
        Long id = 1L;
        CategoriaRequestDTO peticion = new CategoriaRequestDTO(
            "Computacion", "Productos sobre computacion"
        );
        when(categoriaService.actualizarCategoriaPorId(id, peticion)).thenThrow(new ConflictException());

        ResponseEntity<CategoriaResponseDTO> respuestaHTTP = categoriaController.actualizarCategoriaPorId(id, peticion);

        assertEquals(HttpStatus.CONFLICT, respuestaHTTP.getStatusCode());
    }

    // ##################################################

    @Test
    void borrarCategoria_204() {
        Long id = 1L;

        ResponseEntity<Void> respuestaHTTP = categoriaController.borrarCategoriaPorId(id);

        assertEquals(HttpStatus.NO_CONTENT, respuestaHTTP.getStatusCode());
    }

    @Test
    void borrarCategoria_400() {
        Long id = 1L;
        doThrow(new NotFoundException()).when(categoriaService).borrarCategoriaPorId(id);

        ResponseEntity<Void> respuestaHTTP = categoriaController.borrarCategoriaPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, respuestaHTTP.getStatusCode());
    }

    @Test
    void borrarCategoria_409() {
        Long id = 1L;
        doThrow(new ConflictException()).when(categoriaService).borrarCategoriaPorId(id);

        ResponseEntity<Void> respuestaHTTP = categoriaController.borrarCategoriaPorId(id);

        assertEquals(HttpStatus.CONFLICT, respuestaHTTP.getStatusCode());
    }

}
