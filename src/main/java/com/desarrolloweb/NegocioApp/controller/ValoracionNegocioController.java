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

import com.desarrolloweb.NegocioApp.dto.valoracionNegocio.ValoracionNegocioResponseDTO;
import com.desarrolloweb.NegocioApp.dto.valoracionNegocio.ValoracionNegocioRequestDTO;
import com.desarrolloweb.NegocioApp.entity.ValoracionNegocio;
import com.desarrolloweb.NegocioApp.service.ValoracionNegocioService;

@RestController
@RequestMapping("/valoraciones-negocio")
public class ValoracionNegocioController {
    @org.springframework.beans.factory.annotation.Autowired
    private com.desarrolloweb.NegocioApp.service.AccessGuard guard;


    private final ValoracionNegocioService valoracionService;

    public ValoracionNegocioController(ValoracionNegocioService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @GetMapping
    public ResponseEntity<List<ValoracionNegocioResponseDTO>> obtenerTodasLasValoraciones() {
        return ResponseEntity.ok(valoracionService.obtenerTodas().stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ValoracionNegocioResponseDTO> obtenerValoracionPorId(@PathVariable Long id) {
        return valoracionService.obtenerPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ValoracionNegocioResponseDTO> crearValoracion(
            @RequestBody ValoracionNegocioRequestDTO request) {
        guard.customerOnly();
        if (request == null) throw new IllegalArgumentException("Valoracion obligatoria");
        if (request.getClienteId() != null && !request.getClienteId().equals(guard.uid())) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        guard.createsBusinessRating(request.getNegocioId(), guard.uid());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(valoracionService.crear(request, guard.uid())));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ValoracionNegocioResponseDTO> actualizarValoracion(
            @PathVariable Long id, @RequestBody ValoracionNegocioRequestDTO request) {
        guard.updatesBusinessRating(id);
        if (request == null) throw new IllegalArgumentException("Valoracion obligatoria");
        return valoracionService.actualizar(id, request)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long id) {
        guard.updatesBusinessRating(id);

        if (!valoracionService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        valoracionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/negocios/{id}/valoraciones")
    public ResponseEntity<List<ValoracionNegocioResponseDTO>> obtenerValoracionesPorNegocio(
            @PathVariable Long id) {
        return ResponseEntity.ok(valoracionService.obtenerPorNegocio(id).stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/clientes/{id}/valoraciones-negocios")
    public ResponseEntity<List<ValoracionNegocioResponseDTO>> obtenerValoracionesPorCliente(
            @PathVariable Long id) {
        guard.selfOrAdmin(id);

        return ResponseEntity.ok(valoracionService.obtenerPorCliente(id).stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/estrellas/{estrellas}")
    public ResponseEntity<?> obtenerValoracionesPorEstrellas(@PathVariable Integer estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Map.of("data", valoracionService.obtenerPorEstrellas(estrellas).stream()
                .map(this::toResponse)
                .collect(Collectors.toList())));
    }

    private ValoracionNegocioResponseDTO toResponse(ValoracionNegocio valoracion) {
        return new ValoracionNegocioResponseDTO(
                valoracion.getId(),
                valoracion.getNegocio().getId(),
                valoracion.getCliente().getId(),
                valoracion.getEstrellas(),
                valoracion.getComentario(),
                valoracion.getFecha());
    }
}
