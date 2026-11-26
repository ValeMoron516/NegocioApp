package com.desarrolloweb.NegocioApp.dto.negocio;

public class NegocioResponseDTO{
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private String aliasBancario;
    private String titularCuenta;
    private String whatsappComercial;
    
    public NegocioResponseDTO(){
    }
    public NegocioResponseDTO(Long id, Long usuarioId, String nombre, String descripcion, String imagenUrl){
        this(id, usuarioId, nombre, descripcion, imagenUrl, null, null, null);
    }
    public NegocioResponseDTO(Long id, Long usuarioId, String nombre, String descripcion, String imagenUrl,
            String aliasBancario, String titularCuenta, String whatsappComercial){
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.aliasBancario = aliasBancario;
        this.titularCuenta = titularCuenta;
        this.whatsappComercial = whatsappComercial;
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
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
        this.nombre = nombre;
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
