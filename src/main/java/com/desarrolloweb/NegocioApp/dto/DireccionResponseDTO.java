package com.desarrolloweb.NegocioApp.dto;

public class DireccionResponseDTO {
    private Long id;
    private Long usuarioId;
    private String calle;
    private String numero;
    private String codigoPostal;
    private String ciudad;

    public DireccionResponseDTO(){

    }

    public DireccionResponseDTO(Long id, Long usuarioId, String calle, String numero, String codigoPostal,
            String ciudad) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.calle = calle;
        this.numero = numero;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

}
