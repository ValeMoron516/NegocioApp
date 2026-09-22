package com.desarrolloweb.NegocioApp.dtos.productoDTO;

import java.math.BigDecimal;


public class ProductoResponseDTO {

    private Long id;
    private Long negocioId;
    private String nombreNegocio;
    private Long categoriaId;
    private String nombreCategoria;
    private String nombre;
    private String descripcion;
    private double precio;
    private Integer stock;

    // Constructores
    public ProductoResponseDTO() { }

    public ProductoResponseDTO(Long id, Long negocioId, String nombreNegocio, 
            Long categoriaId, String nombreCategoria, String nombre,
            String descripcion, double precio, Integer stock) {
        this.id = id;
        this.negocioId = negocioId;
        this.nombreNegocio = nombreNegocio;
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNegocioId() { return negocioId; }
    public void setNegocioId(Long negocioId) { this.negocioId = negocioId; }

    public String getNombreNegocio() { return nombreNegocio; }
    public void setNombreNegocio(String nombreNegocio) { this.nombreNegocio = nombreNegocio; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
        
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
}
