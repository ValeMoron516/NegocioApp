package com.desarrolloweb.NegocioApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    private Long id;

    @Column(length = 50, nullable = false, unique = true) // Ej: CLIENTE, NEGOCIO
    private String nombre;

    // Constructores
    public Rol() { }

    public Rol(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}