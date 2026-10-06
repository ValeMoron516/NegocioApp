package com.desarrolloweb.NegocioApp.dto;

public class ValoracionProductoRequestDTO {
    private Long productoId;
    private int puntuacion;
    private String comentario;

    public ValoracionProductoRequestDTO (){

    }

    public ValoracionProductoRequestDTO(Long productoId, int puntuacion, String comentario) {
        this.productoId = productoId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


}
