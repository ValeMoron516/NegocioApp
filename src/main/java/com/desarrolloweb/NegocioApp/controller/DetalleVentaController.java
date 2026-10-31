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

import com.desarrolloweb.NegocioApp.dto.DetalleVentaRequest;
import com.desarrolloweb.NegocioApp.dto.DetalleVentaResponse;
import com.desarrolloweb.NegocioApp.service.DetalleVentaService;

@RestController
@RequestMapping("/api/v1/detalles-ventas")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    // Ruta nueva y alias de la ruta antigua para no romper clientes existentes.
    @PostMapping({"", "/detalle-venta/{id}"})
    public ResponseEntity<DetalleVentaResponse> crearDetalleVenta(
            @RequestBody DetalleVentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detalleVentaService.crear(request));
    }

    @GetMapping({"", "/detalles-ventas"})
    public ResponseEntity<List<DetalleVentaResponse>> obtenerTodosLosDetalles() {
        List<DetalleVentaResponse> detalles = detalleVentaService.obtenerTodos();
        return detalles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(detalles);
    }

    @GetMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVentaResponse> obtenerDetallePorId(@PathVariable Long id) {
        return detalleVentaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/detalle-venta/{id}")
    public ResponseEntity<DetalleVentaResponse> actualizarDetalleVenta(
            @PathVariable Long id, @RequestBody DetalleVentaRequest request) {
        return detalleVentaService.actualizarCantidad(id, request.getCantidad())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/detalle-venta/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        detalleVentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/venta/{id}/detalles")
    public ResponseEntity<List<DetalleVentaResponse>> obtenerDetallesPorVenta(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.obtenerPorVenta(id));
    }

    @GetMapping("/productos/{id}/detalles-ventas")
    public ResponseEntity<List<DetalleVentaResponse>> obtenerDetallesPorProducto(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.obtenerPorProducto(id));
    }
}
