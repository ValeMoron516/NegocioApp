package com.desarrolloweb.NegocioApp.dto;

public class NegocioResponseDto{
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    public NegocioResponseDto(){
    }
    public NegocioResponseDto(Long id, Long usuarioId, String nombre, String descripcion){
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
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
}
