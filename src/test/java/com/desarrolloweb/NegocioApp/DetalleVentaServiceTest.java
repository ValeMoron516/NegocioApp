package com.desarrolloweb.NegocioApp;

import com.desarrolloweb.NegocioApp.dto.DetalleVentaRequest;
import com.desarrolloweb.NegocioApp.dto.DetalleVentaResponse;
import com.desarrolloweb.NegocioApp.entity.*;
import com.desarrolloweb.NegocioApp.repository.*;
import com.desarrolloweb.NegocioApp.service.DetalleVentaService;

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
class DetalleVentaServiceTest {

    @Mock DetalleVentaRepository detalleVentaRepository;
    @Mock VentaRepository ventaRepository;
    @Mock ProductoRepository productoRepository;
    @InjectMocks DetalleVentaService detalleVentaService;

    private Negocio negocio() {
        Negocio n = new Negocio();
        n.setId(1L);
        return n;
    }

    private Venta venta(Negocio n, String estado, String total) {
        Venta v = new Venta();
        v.setId(10L);
        v.setNegocio(n);
        v.setEstado(estado);
        v.setTotal(new BigDecimal(total));
        return v;
    }

    private Producto producto(Negocio n, int stock) {
        return new Producto(5L, n, null, "Auriculares", "Bluetooth",
                new BigDecimal("45000.00"), stock);
    }

    @Test
    void crearDetalleActualizaStockYTotal() {
        Negocio n = negocio();
        Venta v = venta(n, "PENDIENTE", "0.00");
        Producto p = producto(n, 5);
        when(ventaRepository.buscarParaModificar(10L)).thenReturn(Optional.of(v));
        when(productoRepository.buscarParaVenta(5L)).thenReturn(Optional.of(p));
        when(detalleVentaRepository.save(any(DetalleVenta.class))).thenAnswer(inv -> {
            DetalleVenta d = inv.getArgument(0);
            d.setId(15L);
            return d;
        });
        DetalleVentaResponse res = detalleVentaService.crear(
                new DetalleVentaRequest(10L, 5L, 2));
        assertEquals(3, p.getStock());
        assertEquals(new BigDecimal("90000.00"), v.getTotal());
        assertEquals(15L, res.getId());
        assertEquals("Auriculares", res.getNombreProducto());
        verify(detalleVentaRepository).save(any(DetalleVenta.class));
    }

    @Test
    void actualizarCantidadRecalculaStockYTotal() {
        Negocio n = negocio();
        Venta v = venta(n, "PENDIENTE", "45000.00");
        Producto p = producto(n, 5);
        DetalleVenta d = new DetalleVenta(20L, v, p, 1, new BigDecimal("45000.00"));
        when(detalleVentaRepository.findById(20L)).thenReturn(Optional.of(d));
        when(ventaRepository.buscarParaModificar(10L)).thenReturn(Optional.of(v));
        when(productoRepository.buscarParaVenta(5L)).thenReturn(Optional.of(p));
        when(detalleVentaRepository.save(d)).thenReturn(d);
        DetalleVentaResponse res = detalleVentaService.actualizarCantidad(20L, 3).orElseThrow();
        assertEquals(3, res.getCantidad());
        assertEquals(3, p.getStock());
        assertEquals(new BigDecimal("135000.00"), v.getTotal());
    }

    @Test
    void stockInsuficienteNoCambiaDatos() {
        Negocio n = negocio();
        Venta v = venta(n, "PENDIENTE", "45000.00");
        Producto p = producto(n, 1);
        DetalleVenta d = new DetalleVenta(20L, v, p, 1, new BigDecimal("45000.00"));
        when(detalleVentaRepository.findById(20L)).thenReturn(Optional.of(d));
        when(ventaRepository.buscarParaModificar(10L)).thenReturn(Optional.of(v));
        when(productoRepository.buscarParaVenta(5L)).thenReturn(Optional.of(p));
        assertThrows(IllegalArgumentException.class,
                () -> detalleVentaService.actualizarCantidad(20L, 3));
        assertEquals(1, p.getStock());
        assertEquals(new BigDecimal("45000.00"), v.getTotal());
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void eliminarDetalleDevuelveStockYReduceTotal() {
        Negocio n = negocio();
        Venta v = venta(n, "PENDIENTE", "90000.00");
        Producto p = producto(n, 3);
        DetalleVenta d = new DetalleVenta(20L, v, p, 2, new BigDecimal("45000.00"));
        when(detalleVentaRepository.findById(20L)).thenReturn(Optional.of(d));
        when(ventaRepository.buscarParaModificar(10L)).thenReturn(Optional.of(v));
        when(productoRepository.buscarParaVenta(5L)).thenReturn(Optional.of(p));
        detalleVentaService.eliminar(20L);
        assertEquals(5, p.getStock());
        assertEquals(new BigDecimal("0.00"), v.getTotal());
        verify(detalleVentaRepository).delete(d);
    }

    @Test
    void detalleNoExponeEntidadUsuario() {
        Negocio n = negocio();
        Venta v = venta(n, "PENDIENTE", "45000.00");
        Producto p = producto(n, 3);
        DetalleVenta d = new DetalleVenta(20L, v, p, 1, new BigDecimal("45000.00"));
        when(detalleVentaRepository.findAll()).thenReturn(List.of(d));
        List<DetalleVentaResponse> res = detalleVentaService.obtenerTodos();
        assertEquals(1, res.size());
        assertEquals(5L, res.get(0).getProductoId());
        assertThrows(NoSuchMethodException.class,
                () -> DetalleVentaResponse.class.getMethod("getPassword"));
    }

    @Test
    void ventaEntregadaNoPermiteModificarDetalles() {
        Negocio n = negocio();
        Venta v = venta(n, "ENTREGADO", "45000.00");
        when(ventaRepository.buscarParaModificar(10L)).thenReturn(Optional.of(v));
        assertThrows(IllegalStateException.class,
                () -> detalleVentaService.crear(new DetalleVentaRequest(10L, 5L, 1)));
        verify(detalleVentaRepository, never()).save(any(DetalleVenta.class));
    }
}
