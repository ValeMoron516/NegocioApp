package com.desarrolloweb.NegocioApp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desarrolloweb.NegocioApp.entity.DetalleVenta;
import com.desarrolloweb.NegocioApp.repository.DetalleVentaRepository;

@RestController
@RequestMapping("/api/v1")
public class DetalleVentaController {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaController(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @PostMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVenta> crearDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detalleVentaRepository.save(detalleVenta));
    }

    @GetMapping("/detalles-ventas")
    public ResponseEntity<List<DetalleVenta>> obtenerTodosLosDetalles() {
        List<DetalleVenta> detalles = detalleVentaRepository.findAll();
        return detalles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(detalles);
    }

    @GetMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetallePorId(@PathVariable Long id) {
        return detalleVentaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(
            @PathVariable Long id, @RequestBody DetalleVenta detalleActualizado) {
        return detalleVentaRepository.findById(id)
                .map(detalle -> {
                    if (detalleActualizado.getCantidad() != null) {
                        detalle.setCantidad(detalleActualizado.getCantidad());
                    }
                    return ResponseEntity.ok(detalleVentaRepository.save(detalle));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/detalle-venta/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        if (!detalleVentaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        detalleVentaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/venta/{id}/detalles")
    public ResponseEntity<List<DetalleVenta>> obtenerDetallesPorVenta(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaRepository.findByVentaId(id));
    }

    @GetMapping("/productos/{id}/detalles-ventas")
    public ResponseEntity<List<DetalleVenta>> obtenerDetallesPorProducto(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaRepository.findByProductoId(id));
    }
}