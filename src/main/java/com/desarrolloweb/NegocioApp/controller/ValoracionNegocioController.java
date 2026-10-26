package com.desarrolloweb.NegocioApp.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desarrolloweb.NegocioApp.dto.ValoracionNegocioResponse;
import com.desarrolloweb.NegocioApp.entity.ValoracionNegocio;
import com.desarrolloweb.NegocioApp.service.ValoracionNegocioService;

@RestController
@RequestMapping("/api/v1/valoraciones-negocio")
public class ValoracionNegocioController {

    private final ValoracionNegocioService valoracionService;

    public ValoracionNegocioController(ValoracionNegocioService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @GetMapping("/valoraciones-negocios")
    public ResponseEntity<List<ValoracionNegocioResponse>> obtenerTodasLasValoraciones() {
        return ResponseEntity.ok(valoracionService.obtenerTodas().stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<ValoracionNegocioResponse> obtenerValoracionPorId(@PathVariable Long id) {
        return valoracionService.obtenerPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/valoraciones-negocios")
    public ResponseEntity<ValoracionNegocioResponse> crearValoracion(
            @RequestBody ValoracionNegocio valoracion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(valoracionService.crear(valoracion)));
    }

    @PatchMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<ValoracionNegocioResponse> actualizarValoracion(
            @PathVariable Long id, @RequestBody ValoracionNegocio valoracionActualizada) {
        return valoracionService.actualizar(id, valoracionActualizada)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long id) {
        if (!valoracionService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        valoracionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/negocios/{id}/valoraciones")
    public ResponseEntity<List<ValoracionNegocioResponse>> obtenerValoracionesPorNegocio(
            @PathVariable Long id) {
        return ResponseEntity.ok(valoracionService.obtenerPorNegocio(id).stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/clientes/{id}/valoraciones-negocios")
    public ResponseEntity<List<ValoracionNegocioResponse>> obtenerValoracionesPorCliente(
            @PathVariable Long id) {
        return ResponseEntity.ok(valoracionService.obtenerPorCliente(id).stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/valoraciones-negocios/estrellas/{estrellas}")
    public ResponseEntity<?> obtenerValoracionesPorEstrellas(@PathVariable Integer estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Map.of("data", valoracionService.obtenerPorEstrellas(estrellas).stream()
                .map(this::toResponse)
                .collect(Collectors.toList())));
    }

    private ValoracionNegocioResponse toResponse(ValoracionNegocio valoracion) {
        return new ValoracionNegocioResponse(
                valoracion.getId(),
                valoracion.getNegocio().getId(),
                valoracion.getCliente().getId(),
                valoracion.getEstrellas(),
                valoracion.getComentario(),
                valoracion.getFecha());
    }
}