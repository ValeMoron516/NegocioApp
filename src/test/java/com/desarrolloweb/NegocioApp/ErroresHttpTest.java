package com.desarrolloweb.NegocioApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.desarrolloweb.NegocioApp.controller.ApiExceptionHandler;
import com.desarrolloweb.NegocioApp.controller.AuthController;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.repository.RolRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.service.AccessGuard;
import com.desarrolloweb.NegocioApp.service.AuthService;
import com.desarrolloweb.NegocioApp.service.CategoriaService;
import com.desarrolloweb.NegocioApp.service.RolService;
import com.desarrolloweb.NegocioApp.service.UsuarioService;

class ErroresHttpTest {

    @Test
    void loginNuloNoProduce500() {
        var controller = new AuthController(mock(AuthService.class), mock(AccessGuard.class));
        assertThrows(BadRequestException.class, () -> controller.login(null));
    }

    @Test
    void categoriaNulaYNombreVacioDan400() {
        var service = new CategoriaService();
        assertThrows(BadRequestException.class, () -> service.crearCategoria(null));
        assertThrows(BadRequestException.class, () -> service.actualizarCategoriaPorId(1L, null));
        assertThrows(BadRequestException.class, () -> service.crearCategoria(
                new com.desarrolloweb.NegocioApp.dto.categoria.CategoriaRequestDTO("   ", "ok")));
        assertThrows(BadRequestException.class, () -> service.obtenerTodasCategorias(1, 0));
    }

    @Test
    void rolNuloDa400() {
        var service = new RolService(mock(RolRepository.class), mock(UsuarioRepository.class));
        assertThrows(BadRequestException.class, () -> service.crearRol(null));
        assertThrows(BadRequestException.class, () -> service.actualizarRolPorId(1L, null));
    }

    @Test
    void usuarioNuloDa400() {
        var service = new UsuarioService(mock(UsuarioRepository.class),
                mock(com.desarrolloweb.NegocioApp.repository.NegocioRepository.class),
                mock(com.desarrolloweb.NegocioApp.repository.DireccionRepository.class),
                mock(com.desarrolloweb.NegocioApp.repository.VentaRepository.class),
                mock(org.springframework.security.crypto.password.PasswordEncoder.class));
        assertThrows(BadRequestException.class, () -> service.crearUsuario(null));
        assertThrows(BadRequestException.class, () -> service.actualizarUsuarioPorId(1L, null));
    }

    @Test
    void respuestasControladasTienenCodigosCorrectos() {
        var handler = new ApiExceptionHandler();
        assertEquals(HttpStatus.BAD_REQUEST, handler.invalid(new BadRequestException("test")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, handler.missing(new NotFoundException("test")).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, handler.conflict(new ConflictException("test")).getStatusCode());
    }

    @Test
    void excepcionInesperadaNoExponeDetalleEnJson() {
        var handler = new ApiExceptionHandler();
        var respuesta = handler.unexpected(new IllegalStateException("dato-privado-de-prueba"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertEquals("Error interno del servidor", respuesta.getBody().get("error"));
    }
}
