package com.desarrolloweb.NegocioApp.controller;

import java.util.Map;
import com.desarrolloweb.NegocioApp.service.AuthService;
import com.desarrolloweb.NegocioApp.service.AccessGuard;
import com.desarrolloweb.NegocioApp.dto.usuario.UsuarioRequestDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService auth;
  private final AccessGuard guard;
  public AuthController(AuthService auth,AccessGuard guard){this.auth=auth;this.guard=guard;}
  public record LoginRequest(String email,String password){}
  @PostMapping("/login")
  public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest request) {
    if (request == null) throw new BadRequestException("Datos de inicio de sesion obligatorios");
    return ResponseEntity.ok(auth.iniciar(request.email(),request.password()));
  }
  @PostMapping("/registro")
  public ResponseEntity<Map<String,Object>> registro(@RequestBody UsuarioRequestDTO request) {
    return ResponseEntity.status(201).body(auth.registrar(request));
  }
  @GetMapping("/me")
  public Map<String,Object> me(){return guard.perfil();}
}
