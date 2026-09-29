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
import com.desarrolloweb.NegocioApp.service.DetalleVentaService;

@RestController
@RequestMapping("/api/v1")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @PostMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVenta> crearDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detalleVentaService.crear(detalleVenta));
    }

    @GetMapping("/detalles-ventas")
    public ResponseEntity<List<DetalleVenta>> obtenerTodosLosDetalles() {
        List<DetalleVenta> detalles = detalleVentaService.obtenerTodos();
        return detalles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(detalles);
    }

    @GetMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetallePorId(@PathVariable Long id) {
        return detalleVentaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(
            @PathVariable Long id, @RequestBody DetalleVenta detalleActualizado) {
        return detalleVentaService.actualizarCantidad(id, detalleActualizado.getCantidad())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/detalle-venta/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        if (!detalleVentaService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        detalleVentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/venta/{id}/detalles")
    public ResponseEntity<List<DetalleVenta>> obtenerDetallesPorVenta(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.obtenerPorVenta(id));
    }

    @GetMapping("/productos/{id}/detalles-ventas")
    public ResponseEntity<List<DetalleVenta>> obtenerDetallesPorProducto(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.obtenerPorProducto(id));
    }
}