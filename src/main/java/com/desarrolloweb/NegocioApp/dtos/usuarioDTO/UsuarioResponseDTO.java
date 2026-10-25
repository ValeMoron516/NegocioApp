package com.desarrolloweb.NegocioApp.dtos.usuarioDTO;

import java.time.LocalDateTime;

// IMPORTANTE: este DTO NO incluye password ni passwordHash.
public class UsuarioResponseDTO {

    private Long id;
    private String telefono;
    private Long rolId;
    private String rolNombre;
    private LocalDateTime creadoEn;
    private LocalDateTime ultimoAcceso;

    // Constructores
    public UsuarioResponseDTO() { }

    public UsuarioResponseDTO(Long id, String telefono, Long rolId, String rolNombre,
            LocalDateTime creadoEn, LocalDateTime ultimoAcceso) {
        this.id = id;
        this.telefono = telefono;
        this.rolId = rolId;
        this.rolNombre = rolNombre;
        this.creadoEn = creadoEn;
        this.ultimoAcceso = ultimoAcceso;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }

    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
}
