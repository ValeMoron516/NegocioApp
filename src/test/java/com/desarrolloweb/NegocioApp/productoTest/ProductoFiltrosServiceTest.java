package com.desarrolloweb.NegocioApp.productoTest;

import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoResponseDTO;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoFiltrosServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @SuppressWarnings("unchecked")
    @Test
    void buscarMonitorUsaConsultaConFiltros() {
        when(productoRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(Page.empty(PageRequest.of(0, 20)));

        PaginacionDTO<ProductoResponseDTO> resultado = productoService.obtenerTodosProductos(
                1, 20, "monitor", null, null, null);

        assertTrue(resultado.getData().isEmpty());
        verify(productoRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(productoRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void precioMinimoMayorAlMaximoRechazado() {
        assertThrows(BadRequestException.class, () -> productoService.obtenerTodosProductos(
                1, 20, null, null, new BigDecimal("500"), new BigDecimal("100")));
        verifyNoInteractions(productoRepository);
    }

    @Test
    void sinFiltrosConservaListadoOriginal() {
        when(productoRepository.findAll(any(Pageable.class)))
                .thenReturn(Page.empty(PageRequest.of(0, 20)));

        PaginacionDTO<ProductoResponseDTO> resultado = productoService.obtenerTodosProductos(
                1, 20, "   ", null, null, null);

        assertTrue(resultado.getData().isEmpty());
        verify(productoRepository).findAll(any(Pageable.class));
    }
}
