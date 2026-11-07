
package com.desarrolloweb.NegocioApp.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.desarrolloweb.NegocioApp.service.ValoracionProductoService;
import com.desarrolloweb.NegocioApp.dto.valoracionProducto.ValoracionProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dto.valoracionProducto.ValoracionProductoResponseDTO;
import com.desarrolloweb.NegocioApp.dto.valoracionProducto.ValoracionProductoPromedioDTO;

import java.util.List;


@RestController
@RequestMapping("/api/v1/valoraciones")


public class ValoracionProductoController {
    @org.springframework.beans.factory.annotation.Autowired
    private com.desarrolloweb.NegocioApp.service.AccessGuard guard;


    private final ValoracionProductoService valoracionProductoService;

    public ValoracionProductoController(ValoracionProductoService valoracionProductoService) {
        this.valoracionProductoService = valoracionProductoService;
    }

    @PostMapping
    public ResponseEntity<ValoracionProductoResponseDTO> crearValoracion(@RequestBody ValoracionProductoRequestDTO dto) {
        guard.customerOnly();
        // Nunca confiar en usuarioId proporcionado por el cliente.
        if (dto == null) throw new IllegalArgumentException("Valoracion obligatoria");
        if (dto.getUsuarioId() != null && !dto.getUsuarioId().equals(guard.uid())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "No autorizado");
        }
        dto.setUsuarioId(guard.uid());
        guard.createsProductRating(dto);

        ValoracionProductoResponseDTO creada = valoracionProductoService.crearValoracion(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // GET /api/v1/valoraciones: todas
    // GET /api/v1/valoraciones?productoId=1: filtradas (comportamiento existente)
    @GetMapping
    public ResponseEntity<List<ValoracionProductoResponseDTO>> obtenerValoraciones(
            @RequestParam(required = false) Long productoId) {
        List<ValoracionProductoResponseDTO> lista = (productoId == null)
            ? valoracionProductoService.listarTodasLasValoraciones()
            : valoracionProductoService.obtenerPorProducto(productoId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/promedio")
    public ResponseEntity<ValoracionProductoPromedioDTO> obtenerPromedio(@RequestParam Long productoId) {
    ValoracionProductoPromedioDTO promedio = valoracionProductoService.obtenerPromedio(productoId);
    return ResponseEntity.ok(promedio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long id) {
        guard.deletesProductRating(id);

    valoracionProductoService.eliminarValoracion(id);
    return ResponseEntity.noContent().build();
    }


}
