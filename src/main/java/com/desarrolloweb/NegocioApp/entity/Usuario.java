package com.desarrolloweb.NegocioApp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    private Long id;

    @Column(length = 20, nullable = false, unique = true) // Login por telefono
    private String telefono;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Rol rol;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    // Constructores
    public Usuario() { }

    public Usuario(Long id, String telefono, String passwordHash, Rol rol,
            LocalDateTime creadoEn, LocalDateTime ultimoAcceso) {
        this.id = id;
        this.telefono = telefono;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.creadoEn = creadoEn;
        this.ultimoAcceso = ultimoAcceso;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
}