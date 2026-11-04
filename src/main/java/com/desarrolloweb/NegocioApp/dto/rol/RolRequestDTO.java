package com.desarrolloweb.NegocioApp.dto.rol;

public class RolRequestDTO {

    private String nombre;

    // Constructores
    public RolRequestDTO() { }

    public RolRequestDTO(String nombre) {
        this.nombre = nombre;
    }

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
