package com.desarrolloweb.NegocioApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desarrolloweb.NegocioApp.dto.venta.VentaRequestDTO;
import com.desarrolloweb.NegocioApp.dto.venta.VentaResponseDTO;
import com.desarrolloweb.NegocioApp.service.VentaService;
import java.util.List;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {
    @org.springframework.beans.factory.annotation.Autowired
    private com.desarrolloweb.NegocioApp.service.AccessGuard guard;


    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crear(@RequestBody VentaRequestDTO request) {
        guard.createsSale(request);

        VentaResponseDTO creada = ventaService.crear(request);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> buscarPorId(@PathVariable Long id) {
        guard.viewsSale(id);

        VentaResponseDTO venta = ventaService.buscarPorId(id);
        return new ResponseEntity<>(venta, HttpStatus.OK);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<VentaResponseDTO>> buscarPorUsuario(@PathVariable Long id) {
        guard.selfOrAdmin(id);

        List<VentaResponseDTO> ventas = ventaService.buscarPorUsuario(id);
        return new ResponseEntity<>(ventas, HttpStatus.OK);
    }
@GetMapping
public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        guard.adminOnly();

    List<VentaResponseDTO> ventas = ventaService.listarTodo();
    return new ResponseEntity<>(ventas, HttpStatus.OK);
}
@GetMapping("/buscar")
public ResponseEntity<List<VentaResponseDTO>> buscarPorFechas(@RequestParam(name="fecha_desde") LocalDate fechaDesde, @RequestParam(name="fecha_hasta") LocalDate fechaHasta) {
        guard.adminOnly();

    List<VentaResponseDTO> ventas = ventaService.buscarPorFechas(fechaDesde, fechaHasta);
    return new ResponseEntity<>(ventas, HttpStatus.OK);
}
@GetMapping("/negocio/{id}/total")
public ResponseEntity<Long> contarPorNegocio(@PathVariable Long id) {
        guard.ownsBusiness(id);

    Long total = ventaService.contarPorNegocio(id);
    return new ResponseEntity<>(total, HttpStatus.OK);
}
@PutMapping("/{id}")
public ResponseEntity<VentaResponseDTO> actualizar(@PathVariable Long id, @RequestBody VentaRequestDTO request) {
        guard.adminOnly();

    VentaResponseDTO actualizada = ventaService.actualizar(id, request);
    return new ResponseEntity<>(actualizada, HttpStatus.OK);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        guard.adminOnly();

    ventaService.eliminar(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}



    @GetMapping("/mias")
    public ResponseEntity<List<VentaResponseDTO>> misCompras() {
        guard.customerOnly();
        return ResponseEntity.ok(ventaService.buscarPorUsuario(guard.uid()));
    }
    @GetMapping("/negocio/{id}")
    public ResponseEntity<List<VentaResponseDTO>> ventasDelNegocio(@PathVariable Long id) {
        guard.ownsBusiness(id);
        return ResponseEntity.ok(ventaService.listarPorNegocio(id));
    }

    public record CambioEstadoRequest(String estado) {}

    @PatchMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> cambiarEstado(
        @PathVariable Long id, @RequestBody CambioEstadoRequest request) {
        // Puede cambiarla solo el propietario del negocio o un administrador.
        guard.ownsSaleBusiness(id);
        if (request == null || request.estado() == null || request.estado().isBlank())
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado obligatorio");
        return ResponseEntity.ok(ventaService.cambiarEstado(id, request.estado()));
    }

}
