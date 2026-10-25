package com.desarrolloweb.NegocioApp.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dtos.usuarioDTO.UsuarioRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.usuarioDTO.UsuarioResponseDTO;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.repository.DireccionRepository;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.VentaRepository;

@Service
@Transactional
public class UsuarioService {

    private static final int PASSWORD_MIN_CARACTERES = 8;
    private static final int PASSWORD_MAX_BYTES = 72; // Limite de BCrypt

    private final UsuarioRepository usuarioRepository;
    private final NegocioRepository negocioRepository;
    private final DireccionRepository direccionRepository;
    private final VentaRepository ventaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            NegocioRepository negocioRepository,
            DireccionRepository direccionRepository,
            VentaRepository ventaRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.negocioRepository = negocioRepository;
        this.direccionRepository = direccionRepository;
        this.ventaRepository = ventaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ##################################################

    // Obtener lista de usuarios (paginados)
    @Transactional(readOnly = true)
    public PaginacionDTO<UsuarioResponseDTO> obtenerTodosUsuarios(Integer page, Integer limit) {

        if (page == null || page < 1 || limit == null || limit < 1) {
            throw new BadRequestException("Parametros de paginacion invalidos");
        }

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Usuario> paginaUsuarios = usuarioRepository.findAll(pageable);

        List<UsuarioResponseDTO> dtos = new ArrayList<>();
        for (Usuario u : paginaUsuarios.getContent()) {
            dtos.add(aDTO(u));
        }

        MetaDTO meta = new MetaDTO(
                paginaUsuarios.getTotalElements(), // totalItems
                paginaUsuarios.getNumberOfElements(), // itemCount
                paginaUsuarios.getSize(), // itemsPerPage
                paginaUsuarios.getTotalPages(), // totalPages
                paginaUsuarios.getNumber() + 1 // currentPage
        );

        return new PaginacionDTO<>(dtos, meta);
    }

    // ##################################################

    // Obtener Usuario por ID
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El usuario solicitado no existe"));
        return aDTO(u);
    }

    // ##################################################

    // Crear nuevo Usuario
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO uDTO) {

        String nombre = validarTextoObligatorio(uDTO.getNombre(), "Nombre", 100);
        String apellido = validarTextoObligatorio(uDTO.getApellido(), "Apellido", 100);
        String telefono = validarTelefono(uDTO.getTelefono());
        String email = validarEmail(uDTO.getEmail());
        validarPassword(uDTO.getPassword());

        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictException("Ya existe un usuario con el email provisto");
        }

        Usuario newU = new Usuario();
        newU.setNombre(nombre);
        newU.setApellido(apellido);
        newU.setTelefono(telefono);
        newU.setEmail(email);
        newU.setPassword(passwordEncoder.encode(uDTO.getPassword())); // Se guarda SOLO el hash
        newU.setFechaRegistro(LocalDateTime.now());

        return aDTO(usuarioRepository.save(newU));
    }

    // ##################################################

    // Actualizar Usuario por ID (solo se modifican los campos enviados)
    public UsuarioResponseDTO actualizarUsuarioPorId(Long id, UsuarioRequestDTO uDTO) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El usuario con el ID provisto no existe"));

        if (uDTO.getNombre() != null) {
            u.setNombre(validarTextoObligatorio(uDTO.getNombre(), "Nombre", 100));
        }

        if (uDTO.getApellido() != null) {
            u.setApellido(validarTextoObligatorio(uDTO.getApellido(), "Apellido", 100));
        }

        if (uDTO.getTelefono() != null) {
            u.setTelefono(validarTelefono(uDTO.getTelefono()));
        }

        // Email: conflicto solo si pertenece a OTRO usuario
        if (uDTO.getEmail() != null) {
            String email = validarEmail(uDTO.getEmail());
            if (!email.equals(u.getEmail()) && usuarioRepository.existsByEmail(email)) {
                throw new ConflictException("El email provisto ya existe");
            }
            u.setEmail(email);
        }

        // Password (se vuelve a hashear)
        if (uDTO.getPassword() != null) {
            validarPassword(uDTO.getPassword());
            u.setPassword(passwordEncoder.encode(uDTO.getPassword()));
        }

        return aDTO(usuarioRepository.save(u));
    }

    // ##################################################

    // Borrar Usuario por ID
    public void borrarUsuarioPorId(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("El usuario con el ID provisto no existe");
        }

        // No se puede borrar si tiene negocios, direcciones o ventas asociadas
        if (!negocioRepository.findByUsuarioId(id).isEmpty()
                || !direccionRepository.findByUsuarioId(id).isEmpty()
                || !ventaRepository.findByUsuarioId(id).isEmpty()) {
            throw new ConflictException("El usuario provisto no puede eliminarse debido a que tiene datos asociados");
        }

        usuarioRepository.deleteById(id);
    }

    // ##################################################
    // Metodos auxiliares

    private String validarTextoObligatorio(String valor, String campo, int maxLargo) {
        if (valor == null || valor.isBlank()) {
            throw new BadRequestException(campo + " invalido");
        }
        String v = valor.trim();
        if (v.length() > maxLargo) {
            throw new BadRequestException(campo + " invalido: maximo " + maxLargo + " caracteres");
        }
        return v;
    }

    private String validarTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new BadRequestException("Telefono invalido");
        }
        String t = telefono.trim();
        // Opcional '+' y de 6 a 19 digitos (maximo 20 caracteres en total)
        if (!t.matches("\\+?[0-9]{6,19}")) {
            throw new BadRequestException(
                    "Telefono invalido: solo numeros (opcionalmente con +), entre 6 y 19 digitos");
        }
        return t;
    }

    private String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email invalido");
        }
        String e = email.trim().toLowerCase();
        if (e.length() > 150 || !e.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new BadRequestException("Email invalido");
        }
        return e;
    }

    private void validarPassword(String password) {
        if (password == null || password.length() < PASSWORD_MIN_CARACTERES) {
            throw new BadRequestException("Password invalida: minimo " + PASSWORD_MIN_CARACTERES + " caracteres");
        }
        // BCrypt ignora todo lo que pase de 72 bytes: se rechaza en lugar de truncar en
        // silencio
        if (password.getBytes(StandardCharsets.UTF_8).length > PASSWORD_MAX_BYTES) {
            throw new BadRequestException("Password invalida: maximo " + PASSWORD_MAX_BYTES + " bytes");
        }
    }

    // Nunca incluye la password en la respuesta
    private UsuarioResponseDTO aDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getTelefono(),
                u.getEmail(),
                u.getFechaRegistro());
    }
}