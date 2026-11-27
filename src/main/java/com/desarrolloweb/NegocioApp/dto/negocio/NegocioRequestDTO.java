package com.desarrolloweb.NegocioApp.dto.negocio;

public class NegocioRequestDTO{
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private String aliasBancario;
    private String titularCuenta;
    private String whatsappComercial;
    
    public NegocioRequestDTO(){
}
    public Long getUsuarioId(){
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId){
        this.usuarioId = usuarioId;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre= nombre;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public String getAliasBancario() { return aliasBancario; }
    public void setAliasBancario(String aliasBancario) { this.aliasBancario = aliasBancario; }
    public String getTitularCuenta() { return titularCuenta; }
    public void setTitularCuenta(String titularCuenta) { this.titularCuenta = titularCuenta; }
    public String getWhatsappComercial() { return whatsappComercial; }
    public void setWhatsappComercial(String whatsappComercial) { this.whatsappComercial = whatsappComercial; }
}




