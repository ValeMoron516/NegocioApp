
package com.desarrolloweb.NegocioApp.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.desarrolloweb.NegocioApp.service.ValoracionProductoService;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoResponseDTO;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoPromedioDTO;

import java.util.List;


@RestController
@RequestMapping("/api/v1/valoraciones")


public class ValoracionProductoController {

    private final ValoracionProductoService valoracionProductoService;

    public ValoracionProductoController(ValoracionProductoService valoracionProductoService) {
        this.valoracionProductoService = valoracionProductoService;
    }

    @PostMapping
    public ResponseEntity<ValoracionProductoResponseDTO> crearValoracion(@RequestBody ValoracionProductoRequestDTO dto) {
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
    valoracionProductoService.eliminarValoracion(id);
    return ResponseEntity.noContent().build();
    }


}
