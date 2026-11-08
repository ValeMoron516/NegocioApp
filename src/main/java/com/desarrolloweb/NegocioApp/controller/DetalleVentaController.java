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

import com.desarrolloweb.NegocioApp.dto.detalleVenta.DetalleVentaRequestDTO;
import com.desarrolloweb.NegocioApp.dto.detalleVenta.DetalleVentaResponseDTO;
import com.desarrolloweb.NegocioApp.service.DetalleVentaService;

@RestController
@RequestMapping("/api/v1/detalles-ventas")
public class DetalleVentaController {
    @org.springframework.beans.factory.annotation.Autowired
    private com.desarrolloweb.NegocioApp.service.AccessGuard guard;


    private final DetalleVentaService detalleVentaService;

    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    // Ruta nueva y alias de la ruta antigua para no romper clientes existentes.
    @PostMapping
    public ResponseEntity<DetalleVentaResponseDTO> crearDetalleVenta(
            @RequestBody DetalleVentaRequestDTO request) {
        guard.createsDetail(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detalleVentaService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<DetalleVentaResponseDTO>> obtenerTodosLosDetalles() {
        guard.adminOnly();

        List<DetalleVentaResponseDTO> detalles = detalleVentaService.obtenerTodos();
        return detalles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO> obtenerDetallePorId(@PathVariable Long id) {
        guard.viewsDetail(id);

        return detalleVentaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO> actualizarDetalleVenta(
            @PathVariable Long id, @RequestBody DetalleVentaRequestDTO request) {
        guard.managesDetail(id);

        return detalleVentaService.actualizarCantidad(id, request.getCantidad())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        guard.managesDetail(id);

        detalleVentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/venta/{id}/detalles")
    public ResponseEntity<List<DetalleVentaResponseDTO>> obtenerDetallesPorVenta(@PathVariable Long id) {
        guard.viewsSale(id);

        return ResponseEntity.ok(detalleVentaService.obtenerPorVenta(id));
    }

    @GetMapping("/productos/{id}/detalles-ventas")
    public ResponseEntity<List<DetalleVentaResponseDTO>> obtenerDetallesPorProducto(@PathVariable Long id) {
        guard.viewsProductDetails(id);

        return ResponseEntity.ok(detalleVentaService.obtenerPorProducto(id));
    }
}
