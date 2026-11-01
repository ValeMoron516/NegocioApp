package com.desarrolloweb.NegocioApp.dto;

public class ValoracionProductoRequestDTO {
    private Long productoId;
    private Long usuarioId;
    private int estrellas;
    private String comentario;

    public ValoracionProductoRequestDTO (){

    }

    public ValoracionProductoRequestDTO(Long productoId, int estrellas, String comentario) {
        this.productoId = productoId;
        this.estrellas = estrellas;
        this.comentario = comentario;
    }

    public ValoracionProductoRequestDTO(Long usuarioId, Long productoId, int estrellas, String comentario) {
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.estrellas = estrellas;
        this.comentario = comentario;
    }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


}
