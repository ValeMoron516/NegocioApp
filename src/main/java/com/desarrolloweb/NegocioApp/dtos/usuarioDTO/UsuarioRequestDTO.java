package com.desarrolloweb.NegocioApp.dtos.usuarioDTO;

public class UsuarioRequestDTO {

    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password; // Texto plano: solo viaja en el request, nunca se guarda ni se devuelve

    // Constructores
    public UsuarioRequestDTO() { }

    public UsuarioRequestDTO(String nombre, String apellido, String telefono,
            String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
    }

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}