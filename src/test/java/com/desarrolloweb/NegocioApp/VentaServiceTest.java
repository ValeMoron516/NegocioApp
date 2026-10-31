
package com.desarrolloweb.NegocioApp;

import com.desarrolloweb.NegocioApp.dto.VentaRequestDto;
import com.desarrolloweb.NegocioApp.dto.VentaResponseDto;
import com.desarrolloweb.NegocioApp.entity.*;
import com.desarrolloweb.NegocioApp.repository.*;
import com.desarrolloweb.NegocioApp.service.VentaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private NegocioRepository negocioRepository;

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private VentaService ventaService;

    @Test
    void crearVentaCalculaTotalYDescuentaStock() {

        Usuario usuario = mock(Usuario.class);
        Direccion direccion = mock(Direccion.class);
        Negocio negocio = mock(Negocio.class);

        when(usuario.getId()).thenReturn(3L);
        when(direccion.getId()).thenReturn(1L);
        when(direccion.getUsuarioId()).thenReturn(3L);
        when(negocio.getId()).thenReturn(1L);

        Producto producto = new Producto(
                1L,
                negocio,
                null,
                "Auriculares",
                "Auriculares Bluetooth",
                new BigDecimal("45000.00"),
                5
        );

        when(usuarioRepository.findById(3L))
                .thenReturn(Optional.of(usuario));

        when(direccionRepository.findById(1L))
                .thenReturn(Optional.of(direccion));

        when(negocioRepository.findById(1L))
                .thenReturn(Optional.of(negocio));

        when(productoRepository.buscarParaVenta(1L))
                .thenReturn(Optional.of(producto));

        when(ventaRepository.save(any(Venta.class)))
                .thenAnswer(invocacion -> {
                    Venta venta = invocacion.getArgument(0);
                    venta.setId(99L);
                    return venta;
                });

        VentaRequestDto request = new VentaRequestDto();

        request.setUsuarioId(3L);
        request.setDireccionId(1L);
        request.setNegocioId(1L);
        request.setEstado("PENDIENTE");

        request.setProductos(List.of(
                new VentaRequestDto.ProductoCompraDto(1L, 2)
        ));

        VentaResponseDto respuesta = ventaService.crear(request);

        // Dos auriculares de $45.000 = $90.000
        assertEquals(
                new BigDecimal("90000.00"),
                respuesta.getTotal()
        );

        // Stock original 5, se venden 2, quedan 3
        assertEquals(3, producto.getStock());

        // La respuesta debe incluir el producto comprado
        assertEquals(1, respuesta.getProductos().size());
        assertEquals(
                2,
                respuesta.getProductos().get(0).cantidad()
        );

        // Comprobar que se intentaron guardar los detalles
        verify(detalleVentaRepository).saveAll(any());
    }

@Test
void crearVentaSinStockDebeFallar() {

    Usuario usuario = mock(Usuario.class);
    Direccion direccion = mock(Direccion.class);
    Negocio negocio = mock(Negocio.class);

    when(negocio.getId()).thenReturn(1L);
    when(direccion.getUsuarioId()).thenReturn(3L);

    Producto producto = new Producto(
            1L,
            negocio,
            null,
            "Auriculares",
            "Auriculares Bluetooth",
            new BigDecimal("45000.00"),
            1
    );

    when(usuarioRepository.findById(3L))
            .thenReturn(Optional.of(usuario));

    when(direccionRepository.findById(1L))
            .thenReturn(Optional.of(direccion));

    when(negocioRepository.findById(1L))
            .thenReturn(Optional.of(negocio));

    when(productoRepository.buscarParaVenta(1L))
            .thenReturn(Optional.of(producto));

    VentaRequestDto request = new VentaRequestDto();

    request.setUsuarioId(3L);
    request.setDireccionId(1L);
    request.setNegocioId(1L);

    request.setProductos(List.of(
            new VentaRequestDto.ProductoCompraDto(1L, 2)
    ));

    Exception error = assertThrows(
            IllegalArgumentException.class,
            () -> ventaService.crear(request)
    );

    assertTrue(error.getMessage().contains("Stock insuficiente"));

    assertEquals(1, producto.getStock());

    verify(ventaRepository, never()).save(any(Venta.class));
    verify(detalleVentaRepository, never()).saveAll(any());
    verify(productoRepository, never()).save(any(Producto.class));
}


    @Test
    void totalNoPuedeCambiarseDesdePut() {
        Venta venta = new Venta();
        venta.setId(4L);
        venta.setEstado("PENDIENTE");
        venta.setTotal(new BigDecimal("45000.00"));
        when(ventaRepository.buscarParaModificar(4L)).thenReturn(Optional.of(venta));
        VentaRequestDto request = new VentaRequestDto();
        request.setTotal(new BigDecimal("1.00"));
        assertThrows(IllegalArgumentException.class,
                () -> ventaService.actualizar(4L, request));
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void cancelarVentaReponeStockUnaSolaVez() {
        Venta venta = new Venta();
        venta.setId(4L);
        venta.setEstado("PENDIENTE");
        venta.setTotal(new BigDecimal("90000.00"));
        venta.setFechaVenta(java.time.LocalDateTime.now());
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        Direccion direccion = new Direccion();
        direccion.setId(1L);
        Negocio negocio = new Negocio();
        negocio.setId(1L);
        venta.setUsuario(usuario);
        venta.setDireccion(direccion);
        venta.setNegocio(negocio);
        Producto producto = new Producto(1L, negocio, null, "Auriculares",
                "Bluetooth", new BigDecimal("45000.00"), 3);
        DetalleVenta detalle = new DetalleVenta(5L, venta, producto, 2,
                new BigDecimal("45000.00"));
        when(ventaRepository.buscarParaModificar(4L)).thenReturn(Optional.of(venta));
        when(detalleVentaRepository.findByVentaId(4L)).thenReturn(List.of(detalle));
        when(productoRepository.buscarParaVenta(1L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArgument(0));

        VentaRequestDto request = new VentaRequestDto();
        request.setEstado("CANCELADO");
        VentaResponseDto respuesta = ventaService.actualizar(4L, request);
        assertEquals("CANCELADO", respuesta.getEstado());
        assertEquals(5, producto.getStock());
        assertEquals(new BigDecimal("90000.00"), respuesta.getTotal());
        verify(productoRepository, times(1)).save(producto);
    }

}
