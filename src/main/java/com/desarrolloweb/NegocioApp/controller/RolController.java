package com.desarrolloweb.NegocioApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dtos.rolDTO.RolRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.rolDTO.RolResponseDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.service.RolService;

@RestController
@RequestMapping("api/v1/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    // ##################################################

    // Leer todos (roles, paginados)
    @GetMapping
    public ResponseEntity<PaginacionDTO<RolResponseDTO>> obtenerTodosRoles(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "20") Integer limit) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(rolService.obtenerTodosRoles(page, limit)); // 200
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    // ##################################################

    // Leer por ID
    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDTO> obtenerRolPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(rolService.obtenerRolPorId(id)); // 200
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // ##################################################

    // Crear nuevo
    @PostMapping
    public ResponseEntity<RolResponseDTO> crearRol(@RequestBody RolRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
            .body(rolService.crearRol(dto)); // 201
        }
        catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
        catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }

    // ##################################################

    // Actualizar por ID
    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDTO> actualizarRolPorId(
        @PathVariable Long id, @RequestBody RolRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(rolService.actualizarRolPorId(id, dto)); // 200
        }
        catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }

    // ##################################################

    // Borrar por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarRolPorId(@PathVariable Long id) {
        try {
            rolService.borrarRolPorId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }
}
