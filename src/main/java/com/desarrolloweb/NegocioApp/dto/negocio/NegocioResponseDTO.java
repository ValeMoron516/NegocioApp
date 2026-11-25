package com.desarrolloweb.NegocioApp.dto.negocio;

public class NegocioResponseDTO{
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    
    public NegocioResponseDTO(){
    }
    public NegocioResponseDTO(Long id, Long usuarioId, String nombre, String descripcion, String imagenUrl){
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
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
}
