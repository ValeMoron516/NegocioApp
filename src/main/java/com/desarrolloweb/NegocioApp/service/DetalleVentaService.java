package com.desarrolloweb.NegocioApp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrolloweb.NegocioApp.dto.DetalleVentaRequest;
import com.desarrolloweb.NegocioApp.dto.DetalleVentaResponse;
import com.desarrolloweb.NegocioApp.entity.DetalleVenta;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.entity.Venta;
import com.desarrolloweb.NegocioApp.repository.DetalleVentaRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.repository.VentaRepository;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository,
            VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<DetalleVentaResponse> obtenerTodos() {
        return detalleVentaRepository.findAll().stream().map(this::convertir).toList();
    }

    @Transactional(readOnly = true)
    public Optional<DetalleVentaResponse> obtenerPorId(Long id) {
        return detalleVentaRepository.findById(id).map(this::convertir);
    }

    @Transactional(readOnly = true)
    public List<DetalleVentaResponse> obtenerPorVenta(Long ventaId) {
        return detalleVentaRepository.findByVentaId(ventaId).stream().map(this::convertir).toList();
    }

    @Transactional(readOnly = true)
    public List<DetalleVentaResponse> obtenerPorProducto(Long productoId) {
        return detalleVentaRepository.findByProductoId(productoId).stream().map(this::convertir).toList();
    }

    public boolean existe(Long id) {
        return detalleVentaRepository.existsById(id);
    }

    // Agregar un producto a una venta pendiente (sin aceptar entidades ni precios del cliente).
    @Transactional
    public DetalleVentaResponse crear(DetalleVentaRequest request) {
        if (request == null || request.getVentaId() == null || request.getProductoId() == null) {
            throw new IllegalArgumentException("ventaId y productoId son obligatorios");
        }
        validarCantidad(request.getCantidad());

        // Bloqueamos primero la venta para serializar cambios de sus detalles y total.
        Venta venta = obtenerVentaBloqueada(request.getVentaId());
        exigirPendiente(venta);
        Producto producto = obtenerProductoBloqueado(request.getProductoId());
        exigirMismoNegocio(venta, producto);
        if (detalleVentaRepository.existsByVentaIdAndProductoId(venta.getId(), producto.getId())) {
            throw new IllegalArgumentException("El producto ya figura en esta venta; actualiza la cantidad");
        }
        validarStock(producto, request.getCantidad());
        validarPrecio(producto.getPrecio());

        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(producto.getPrecio());

        producto.setStock(producto.getStock() - request.getCantidad());
        venta.setTotal(venta.getTotal().add(producto.getPrecio()
                .multiply(BigDecimal.valueOf(request.getCantidad()))));
        productoRepository.save(producto);
        ventaRepository.save(venta);
        return convertir(detalleVentaRepository.save(detalle));
    }

    // Ajustar la cantidad conservando el precio histórico de la compra.
    @Transactional
    public Optional<DetalleVentaResponse> actualizarCantidad(Long id, Integer cantidad) {
        validarCantidad(cantidad);
        Optional<DetalleVenta> inicial = detalleVentaRepository.findById(id);
        if (inicial.isEmpty()) {
            return Optional.empty();
        }
        Venta venta = obtenerVentaBloqueada(inicial.get().getVenta().getId());
        exigirPendiente(venta);
        DetalleVenta detalle = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Detalle no encontrado"));
        Producto producto = obtenerProductoBloqueado(detalle.getProducto().getId());

        int diferencia = cantidad - detalle.getCantidad();
        if (diferencia > 0) {
            validarStock(producto, diferencia);
        }
        BigDecimal nuevoTotal = venta.getTotal().add(detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(diferencia)));
        if (nuevoTotal.signum() < 0) {
            throw new IllegalStateException("El total de la venta quedaria negativo");
        }
        producto.setStock(producto.getStock() - diferencia);
        detalle.setCantidad(cantidad);
        venta.setTotal(nuevoTotal);
        productoRepository.save(producto);
        ventaRepository.save(venta);
        return Optional.of(convertir(detalleVentaRepository.save(detalle)));
    }

    // Quitar un producto de una venta pendiente, reponer stock y recalcular total.
    @Transactional
    public void eliminar(Long id) {
        DetalleVenta inicial = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Detalle no encontrado"));
        Venta venta = obtenerVentaBloqueada(inicial.getVenta().getId());
        exigirPendiente(venta);
        DetalleVenta detalle = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Detalle no encontrado"));
        // No permitir ventas pendientes vacías: para eso se elimina la venta completa.
        if (detalleVentaRepository.findByVentaId(venta.getId()).size() <= 1) {
            throw new IllegalStateException(
                    "No se puede dejar la venta sin productos; elimina la venta pendiente completa");
        }
        Producto producto = obtenerProductoBloqueado(detalle.getProducto().getId());
        BigDecimal nuevoTotal = venta.getTotal().subtract(detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad())));
        if (nuevoTotal.signum() < 0) {
            throw new IllegalStateException("El total de la venta quedaria negativo");
        }
        producto.setStock(Math.addExact(producto.getStock(), detalle.getCantidad()));
        venta.setTotal(nuevoTotal);
        detalleVentaRepository.delete(detalle);
        productoRepository.save(producto);
        ventaRepository.save(venta);
    }

    private Venta obtenerVentaBloqueada(Long id) {
        return ventaRepository.buscarParaModificar(id)
                .orElseThrow(() -> new NoSuchElementException("Venta no encontrada: " + id));
    }

    private Producto obtenerProductoBloqueado(Long id) {
        return productoRepository.buscarParaVenta(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id));
    }

    private void exigirPendiente(Venta venta) {
        if (venta.getEstado() == null
                || !"PENDIENTE".equalsIgnoreCase(venta.getEstado().trim())) {
            throw new IllegalStateException("Solo se pueden modificar detalles de ventas PENDIENTES");
        }
    }

    private void exigirMismoNegocio(Venta venta, Producto producto) {
        if (!producto.getNegocio().getId().equals(venta.getNegocio().getId())) {
            throw new IllegalArgumentException("El producto no pertenece al negocio de la venta");
        }
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    private void validarStock(Producto producto, int cantidad) {
        if (producto.getStock() == null || producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombre());
        }
    }

    private void validarPrecio(BigDecimal precio) {
        if (precio == null || precio.signum() < 0) {
            throw new IllegalArgumentException("Precio invalido");
        }
    }

    private DetalleVentaResponse convertir(DetalleVenta detalle) {
        DetalleVentaResponse dto = new DetalleVentaResponse(
                detalle.getId(), detalle.getVenta().getId(), detalle.getProducto().getId(),
                detalle.getCantidad(), detalle.getPrecioUnitario());
        dto.setNombreProducto(detalle.getProducto().getNombre());
        return dto;
    }
}
