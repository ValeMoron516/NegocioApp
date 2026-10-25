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
import com.desarrolloweb.NegocioApp.dtos.usuarioDTO.UsuarioRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.usuarioDTO.UsuarioResponseDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.service.UsuarioService;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ##################################################

    // Leer todos (usuarios, paginados)
    @GetMapping
    public ResponseEntity<PaginacionDTO<UsuarioResponseDTO>> obtenerTodosUsuarios(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "20") Integer limit) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(usuarioService.obtenerTodosUsuarios(page, limit)); // 200
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    // ##################################################

    // Leer por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(usuarioService.obtenerUsuarioPorId(id)); // 200
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // ##################################################

    // Crear nuevo
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody UsuarioRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
            .body(usuarioService.crearUsuario(dto)); // 201
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
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuarioPorId(
        @PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
            .body(usuarioService.actualizarUsuarioPorId(id, dto)); // 200
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
    public ResponseEntity<Void> borrarUsuarioPorId(@PathVariable Long id) {
        try {
            usuarioService.borrarUsuarioPorId(id);
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
