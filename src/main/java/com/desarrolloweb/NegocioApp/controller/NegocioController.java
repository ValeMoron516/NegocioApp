package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.entity.Negocio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desarrolloweb.NegocioApp.dto.NegocioRequestDto;
import com.desarrolloweb.NegocioApp.dto.NegocioResponseDto;
import com.desarrolloweb.NegocioApp.service.NegocioService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/negocio")
public class NegocioController {

    private final NegocioService negocioService;

    @Autowired
    public NegocioController(NegocioService negocioService) {
        this.negocioService = negocioService;
    }

    @PostMapping
    public ResponseEntity<NegocioResponseDto> crear(@RequestBody NegocioRequestDto request) {
        NegocioResponseDto creada = negocioService.crear(request);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NegocioResponseDto> buscarPorId(@PathVariable Long id) {
        NegocioResponseDto negocio = negocioService.buscarPorId(id);
        return new ResponseEntity<>(negocio, HttpStatus.OK);
}
@GetMapping("/buscar")
public ResponseEntity<List<NegocioResponseDto>> buscarPorNombre(@RequestParam String nombre) {
    List<NegocioResponseDto> negocios = negocioService.buscarPorNombre(nombre);
    return new ResponseEntity<>(negocios, HttpStatus.OK);
}
@GetMapping("/buscar")
public ResponseEntity<List<NegocioResponseDto>> buscarPorProducto(@RequestParam String producto) {
    List<NegocioResponseDto> negocios = negocioService.buscarPorProducto(producto);
    return new ResponseEntity<>(negocios, HttpStatus.OK);
}
@GetMapping("/api/v1/usuarios/{id}/negocios")
public ResponseEntity<List<NegocioResponseDto>> buscarPorUsuario(@PathVariable Long id) {
    List<NegocioResponseDto> negocios = negocioService.buscarPorUsuario(id);
    return new ResponseEntity<>(negocios, HttpStatus.OK);
}
@PutMapping("/{id}")
public ResponseEntity<NegocioResponseDto> actualizar(@PathVariable Long id, @RequestBody NegocioRequestDto request) {
    NegocioResponseDto actualizada = negocioService.actualizar(id, request);
    return new ResponseEntity<>(actualizada, HttpStatus.OK);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    negocioService.eliminar(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}
}