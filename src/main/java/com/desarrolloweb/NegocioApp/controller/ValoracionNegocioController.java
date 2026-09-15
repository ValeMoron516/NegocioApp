package com.desarrolloweb.NegocioApp.controller;

import java.time.LocalDateTime;
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
import com.desarrolloweb.NegocioApp.repository.ValoracionNegocioRepository;

@RestController
@RequestMapping("/api/v1")
public class ValoracionNegocioController {

    private final ValoracionNegocioRepository valoracionRepository;

    public ValoracionNegocioController(ValoracionNegocioRepository valoracionRepository) {
        this.valoracionRepository = valoracionRepository;
    }

    @GetMapping("/valoraciones-negocios")
    public ResponseEntity<List<ValoracionNegocio>> obtenerTodasLasValoraciones() {
        return ResponseEntity.ok(valoracionRepository.findAll());
    }

    @GetMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<ValoracionNegocio> obtenerValoracionPorId(@PathVariable Long id) {
        return valoracionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/valoraciones-negocios")
    public ResponseEntity<ValoracionNegocio> crearValoracion(
            @RequestBody ValoracionNegocio valoracion) {
        if (valoracion.getFecha() == null) {
            valoracion.setFecha(LocalDateTime.now());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valoracionRepository.save(valoracion));
    }

    @PatchMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<ValoracionNegocio> actualizarValoracion(
            @PathVariable Long id, @RequestBody ValoracionNegocio valoracionActualizada) {
        return valoracionRepository.findById(id)
                .map(valoracion -> {
                    if (valoracionActualizada.getEstrellas() != null) {
                        valoracion.setEstrellas(valoracionActualizada.getEstrellas());
                    }
                    if (valoracionActualizada.getComentario() != null) {
                        valoracion.setComentario(valoracionActualizada.getComentario());
                    }
                    return ResponseEntity.ok(valoracionRepository.save(valoracion));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/valoraciones-negocios/{id}")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long id) {
        if (!valoracionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        valoracionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/negocios/{id}/valoraciones")
    public ResponseEntity<List<ValoracionNegocio>> obtenerValoracionesPorNegocio(
            @PathVariable Long id) {
        return ResponseEntity.ok(valoracionRepository.findByNegocioId(id));
    }

    @GetMapping("/clientes/{id}/valoraciones-negocios")
    public ResponseEntity<List<ValoracionNegocio>> obtenerValoracionesPorCliente(
            @PathVariable Long id) {
        return ResponseEntity.ok(valoracionRepository.findByClienteId(id));
    }

    @GetMapping("/valoraciones-negocios/estrellas/{estrellas}")
    public ResponseEntity<?> obtenerValoracionesPorEstrellas(@PathVariable Integer estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Map.of("data", valoracionRepository.findByEstrellas(estrellas)));
    }
}