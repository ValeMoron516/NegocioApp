
package com.desarrolloweb.NegocioApp.dto;

public class ValoracionProductoResponseDTO {

    private Long id;
    private Long usuarioId;
    private Long productoId;
    private int puntuacion;
    private String comentario;
    private String fecha;

    public ValoracionProductoResponseDTO(){

    }

    public ValoracionProductoResponseDTO(Long id, Long usuarioId, Long productoId, int puntuacion, String comentario,
            String fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


}
