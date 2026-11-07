package com.desarrolloweb.NegocioApp.service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Rol;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.RolRepository;
import com.desarrolloweb.NegocioApp.dto.usuario.UsuarioRequestDTO;
import com.desarrolloweb.NegocioApp.dto.usuario.UsuarioResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
  private final UsuarioRepository usuarios;
  private final RolRepository roles;
  private final UsuarioService usuarioService;
  private final PasswordEncoder encoder;
  private final JwtEncoder jwtEncoder;
  public AuthService(UsuarioRepository usuarios, RolRepository roles, UsuarioService usuarioService,
      PasswordEncoder encoder, JwtEncoder jwtEncoder) {
    this.usuarios=usuarios;this.roles=roles;this.usuarioService=usuarioService;this.encoder=encoder;this.jwtEncoder=jwtEncoder;
  }
  @Transactional
  public Map<String,Object> registrar(UsuarioRequestDTO request) {
    UsuarioResponseDTO registrado=usuarioService.crearUsuario(request);
    Usuario usuario=usuarios.findById(registrado.getId()).orElseThrow();
    Rol cliente=roles.findByNombre("CLIENTE").orElseGet(()->roles.save(new Rol(null,"CLIENTE")));
    usuario.getRoles().add(cliente);
    usuarios.save(usuario);
    return crearRespuesta(usuario);
  }
  @Transactional
  public Map<String,Object> iniciar(String email, String password) {
    if (email==null || password==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales inválidas");
    Usuario usuario=usuarios.findByEmail(email.trim().toLowerCase(Locale.ROOT))
      .orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales inválidas"));
    if (!encoder.matches(password,usuario.getPassword()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales inválidas");
    // Normalizacion de cuentas comerciales anteriores: toda cuenta NEGOCIO tambien compra como CLIENTE.
    boolean comerciante=usuario.getRoles().stream().anyMatch(r->"NEGOCIO".equalsIgnoreCase(r.getNombre()));
    boolean cliente=usuario.getRoles().stream().anyMatch(r->"CLIENTE".equalsIgnoreCase(r.getNombre()));
    if(comerciante && !cliente){
      Rol basico=roles.findByNombre("CLIENTE").orElseGet(()->roles.save(new Rol(null,"CLIENTE")));
      usuario.getRoles().add(basico);
      usuarios.save(usuario);
    }
    return crearRespuesta(usuario);
  }
  private Map<String,Object> crearRespuesta(Usuario usuario) {
    Instant now=Instant.now();
    // Roles are read from the server and never accepted from a registration request.
    List<String> userRoles=usuario.getRoles().stream().map(Rol::getNombre)
      .map(r->r.trim().toUpperCase(Locale.ROOT)).distinct().toList();
    var claims=JwtClaimsSet.builder().issuer("negocioapp").subject(usuario.getId().toString())
      .issuedAt(now).expiresAt(now.plusSeconds(15*60)).claim("roles",userRoles).build();
    String token=jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
    return Map.of("accessToken",token,"tokenType","Bearer","expiresIn",900,
      "usuario", Map.of("id",usuario.getId(),"nombre",usuario.getNombre(),"email",usuario.getEmail(),"roles",userRoles));
  }
}
