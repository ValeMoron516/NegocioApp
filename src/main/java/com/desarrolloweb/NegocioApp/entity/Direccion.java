package com.desarrolloweb.NegocioApp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

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

    // Constructor con datos utilizando los métodos de asignación
    public Direccion(Long id, Usuario usuario, String calle, String numero, String ciudad, String codigoPostal) {
        setId(id);
        setUsuario(usuario);
        setCalle(calle);
        setNumero(numero);
        setCiudad(ciudad);
        setCodigoPostal(codigoPostal);
    }

    // Getters convencionales
    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getCalle() { return calle; }
    public String getNumero() { return numero; }
    public String getCiudad() { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }

    // Setters que juegan con los Getters para controlar el flujo
    public void setId(Long nuevoId) {
        if (nuevoId != getId()) { // Compara usando el getter
            id = nuevoId;
        }
    }

    public void setUsuario(Usuario nuevoUsuario) {
        if (nuevoUsuario != getUsuario()) { 
            usuario = nuevoUsuario;
        }
    }

    public void setCalle(String nuevaCalle) {
        // Valida que la calle nueva no sea igual a la que devuelve getCalle()
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