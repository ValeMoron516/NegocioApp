package com.desarrolloweb.NegocioApp.dto.negocio;

public class NegocioRequestDTO{
    private Long usuarioId;
    private String nombre;
    private String descripcion;
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
}




