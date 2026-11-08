package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.entity.Negocio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desarrolloweb.NegocioApp.dto.negocio.NegocioRequestDTO;
import com.desarrolloweb.NegocioApp.dto.negocio.NegocioResponseDTO;
import com.desarrolloweb.NegocioApp.service.NegocioService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/negocio")
public class NegocioController {
    @org.springframework.beans.factory.annotation.Autowired
    private com.desarrolloweb.NegocioApp.service.AccessGuard guard;


    private final NegocioService negocioService;

    @Autowired
    public NegocioController(NegocioService negocioService) {
        this.negocioService = negocioService;
    }

    @PostMapping
    public ResponseEntity<NegocioResponseDTO> crear(@RequestBody NegocioRequestDTO request) {
        guard.createsBusiness(request);

        NegocioResponseDTO creada = negocioService.crear(request);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NegocioResponseDTO> buscarPorId(@PathVariable Long id) {
        NegocioResponseDTO negocio = negocioService.buscarPorId(id);
        return new ResponseEntity<>(negocio, HttpStatus.OK);
}
@GetMapping("/buscarPorNombre")
public ResponseEntity<List<NegocioResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
    List<NegocioResponseDTO> negocios = negocioService.buscarPorNombre(nombre);
    return new ResponseEntity<>(negocios, HttpStatus.OK);
}
@GetMapping("/buscarPorProducto")
public ResponseEntity<List<NegocioResponseDTO>> buscarPorProducto(@RequestParam String producto) {
    List<NegocioResponseDTO> negocios = negocioService.buscarPorProducto(producto);
    return new ResponseEntity<>(negocios, HttpStatus.OK);
}
@GetMapping("/usuario/{id}")
public ResponseEntity<List<NegocioResponseDTO>> buscarPorUsuario(@PathVariable Long id) {
        guard.selfOrAdmin(id);

    List<NegocioResponseDTO> negocios = negocioService.buscarPorUsuario(id);
    return new ResponseEntity<>(negocios, HttpStatus.OK);
}
@PutMapping("/{id}")
public ResponseEntity<NegocioResponseDTO> actualizar(@PathVariable Long id, @RequestBody NegocioRequestDTO request) {
        guard.updatesBusiness(id,request);

    NegocioResponseDTO actualizada = negocioService.actualizar(id, request);
    return new ResponseEntity<>(actualizada, HttpStatus.OK);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        guard.ownsBusiness(id);

    negocioService.eliminar(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}


    @GetMapping("/mios")
    public ResponseEntity<List<NegocioResponseDTO>> misNegocios() {
        if(!guard.merchant())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(negocioService.buscarPorUsuario(guard.uid()));
    }

}