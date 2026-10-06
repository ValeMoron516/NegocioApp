package com.desarrolloweb.NegocioApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desarrolloweb.NegocioApp.dto.VentaRequestDto;
import com.desarrolloweb.NegocioApp.dto.VentaResponseDto;
import com.desarrolloweb.NegocioApp.service.VentaService;
import java.util.List;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<VentaResponseDto> crear(@RequestBody VentaRequestDto request) {
        VentaResponseDto creada = ventaService.crear(request);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDto> buscarPorId(@PathVariable Long id) {
        VentaResponseDto venta = ventaService.buscarPorId(id);
        return new ResponseEntity<>(venta, HttpStatus.OK);
    }

    @GetMapping("/api/v1/usuarios/{id}/ventas")
    public ResponseEntity<List<VentaResponseDto>> buscarPorUsuario(@PathVariable Long id) {
        List<VentaResponseDto> ventas = ventaService.buscarPorUsuario(id);
        return new ResponseEntity<>(ventas, HttpStatus.OK);
    }
@GetMapping
public ResponseEntity<List<VentaResponseDto>> listarTodas() {
    List<VentaResponseDto> ventas = ventaService.listarTodo();
    return new ResponseEntity<>(ventas, HttpStatus.OK);
}
@GetMapping("/buscar")
public ResponseEntity<List<VentaResponseDto>> buscarPorFechas(@RequestParam(name="fecha_desde") LocalDate fechaDesde, @RequestParam(name="fecha_hasta") LocalDate fechaHasta) {
    List<VentaResponseDto> ventas = ventaService.buscarPorFechas(fechaDesde, fechaHasta);
    return new ResponseEntity<>(ventas, HttpStatus.OK);
}
@GetMapping("/api/v1/negocios/{id}/ventas/total")
public ResponseEntity<Long> contarPorNegocio(@PathVariable Long id) {
    Long total = ventaService.contarPorNegocio(id);
    return new ResponseEntity<>(total, HttpStatus.OK);
}
@PutMapping("/{id}")
public ResponseEntity<VentaResponseDto> actualizar(@PathVariable Long id, @RequestBody VentaRequestDto request) {
    VentaResponseDto actualizada = ventaService.actualizar(id, request);
    return new ResponseEntity<>(actualizada, HttpStatus.OK);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    ventaService.eliminar(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

}