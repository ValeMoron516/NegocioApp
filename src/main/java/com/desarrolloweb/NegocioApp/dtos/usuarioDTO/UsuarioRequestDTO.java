package com.desarrolloweb.NegocioApp.dtos.usuarioDTO;

public class UsuarioRequestDTO {

    private String telefono;
    private String password; // Texto plano: solo viaja en el request, nunca se guarda ni se devuelve
    private Long rolId;

    // Constructores
    public UsuarioRequestDTO() { }

    public UsuarioRequestDTO(String telefono, String password, Long rolId) {
        this.telefono = telefono;
        this.password = password;
        this.rolId = rolId;
    }

    // --- Getters y Setters ---
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }
}
