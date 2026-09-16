package com.desarrolloweb.NegocioApp.dto;

public class ValoracionProductoPromedioDTO {
    private Long productoId;
    private Double puntuacionPromedio;
    private int totalValoraciones;

    public ValoracionProductoPromedioDTO(){}

    public ValoracionProductoPromedioDTO(Long productoId, double puntuacionPromedio, int totalValoraciones) {
        this.productoId = productoId;
        this.puntuacionPromedio = puntuacionPromedio;
        this.totalValoraciones = totalValoraciones;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public double getPuntuacionPromedio() {
        return puntuacionPromedio;
    }

    public void setPuntuacionPromedio(double puntuacionPromedio) {
        this.puntuacionPromedio = puntuacionPromedio;
    }

    public int getTotalValoraciones() {
        return totalValoraciones;
    }

    public void setTotalValoraciones(int totalValoraciones) {
        this.totalValoraciones = totalValoraciones;
    }


}
