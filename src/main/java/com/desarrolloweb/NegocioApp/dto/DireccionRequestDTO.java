package com.desarrolloweb.NegocioApp.dto;

public class DireccionRequestDTO {
    private String calle;
    private String numero;
    private String ciudad;
    private String codigoPostal;
    private Long usuarioId;

    public DireccionRequestDTO(){

    }

    public DireccionRequestDTO(String calle, String numero, String ciudad, String codigoPostal, Long usuarioId) {
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.usuarioId = usuarioId;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }


    public String getCodigoPostal() {
        return codigoPostal;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    

}
