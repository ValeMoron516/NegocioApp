package com.desarrolloweb.NegocioApp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 150)
    private String calle;

    @Column(nullable = false, length = 50)
    private String numero;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(name = "codigo_postal", nullable = false, length = 50)
    private String codigoPostal;

    // Constructor vacío
    public Direccion() {}

    // Constructor con datos
    public Direccion(Long id, Long usuarioId, String calle, String numero, String ciudad, String codigoPostal) {
        setId(id);
        setUsuarioId(usuarioId);
        setCalle(calle);
        setNumero(numero);
        setCiudad(ciudad);
        setCodigoPostal(codigoPostal);
    }

    // Getters
    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getCalle() { return calle; }
    public String getNumero() { return numero; }
    public String getCiudad() { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }

    // Setters
    public void setId(Long nuevoId) {
        if (nuevoId != getId()) {
            id = nuevoId;
        }
    }

    public void setUsuarioId(Long nuevoUsuarioId) {
        if (nuevoUsuarioId != getUsuarioId()) {
            usuarioId = nuevoUsuarioId;
        }
    }

    public void setCalle(String nuevaCalle) {
        if (nuevaCalle != null && !nuevaCalle.equals(getCalle())) {
            calle = nuevaCalle;
        }
    }

    public void setNumero(String nuevoNumero) {
        if (nuevoNumero != null && !nuevoNumero.equals(getNumero())) {
            numero = nuevoNumero;
        }
    }

    public void setCiudad(String nuevaCiudad) {
        if (nuevaCiudad != null && !nuevaCiudad.equals(getCiudad())) {
            ciudad = nuevaCiudad;
        }
    }

    public void setCodigoPostal(String nuevoCodigoPostal) {
        if (nuevoCodigoPostal != null && !nuevoCodigoPostal.equals(getCodigoPostal())) {
            codigoPostal = nuevoCodigoPostal;
        }
    }
}