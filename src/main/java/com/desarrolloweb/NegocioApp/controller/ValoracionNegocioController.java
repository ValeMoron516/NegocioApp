package com.desarrolloweb.NegocioApp.controller;

import java.util.List;
import java.util.Map;

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

import com.desarrolloweb.NegocioApp.entity.ValoracionNegocio;
import com.desarrolloweb.NegocioApp.service.ValoracionNegocioService;

@RestController
@RequestMapping("/api/v1")
public class ValoracionNegocioController {

    private final ValoracionNegocioService valoracionService;

    public ValoracionNegocioController(ValoracionNegocioService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @GetMapping("/valoraciones-negocios")
    public ResponseEntity<List<ValoracionNegocio>> obtenerTodasLasValoraciones() {
        return ResponseEntity.ok(valoracionService.obtenerTodas());
    }

    @GetMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<ValoracionNegocio> obtenerValoracionPorId(@PathVariable Long id) {
        return valoracionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/valoraciones-negocios")
    public ResponseEntity<ValoracionNegocio> crearValoracion(
            @RequestBody ValoracionNegocio valoracion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valoracionService.crear(valoracion));
    }

    @PatchMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<ValoracionNegocio> actualizarValoracion(
            @PathVariable Long id, @RequestBody ValoracionNegocio valoracionActualizada) {
        return valoracionService.actualizar(id, valoracionActualizada)
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
    public ResponseEntity<List<ValoracionNegocio>> obtenerValoracionesPorNegocio(
            @PathVariable Long id) {
        return ResponseEntity.ok(valoracionService.obtenerPorNegocio(id));
    }

    @GetMapping("/clientes/{id}/valoraciones-negocios")
    public ResponseEntity<List<ValoracionNegocio>> obtenerValoracionesPorCliente(
            @PathVariable Long id) {
        return ResponseEntity.ok(valoracionService.obtenerPorCliente(id));
    }

    @GetMapping("/valoraciones-negocios/estrellas/{estrellas}")
    public ResponseEntity<?> obtenerValoracionesPorEstrellas(@PathVariable Integer estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Map.of("data", valoracionService.obtenerPorEstrellas(estrellas)));
    }
}