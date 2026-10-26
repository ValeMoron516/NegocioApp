package com.desarrolloweb.NegocioApp.dtos.categoriaDTO;


public class CategoriaRequestDTO {

    private String nombre;
    private String descripcion;

    // Constructores
    public CategoriaRequestDTO() { }

    public CategoriaRequestDTO(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

