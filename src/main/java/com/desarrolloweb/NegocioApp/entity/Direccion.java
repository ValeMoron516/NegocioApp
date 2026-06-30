package com.desarrolloweb.NegocioApp.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "direcciones")

public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    @Column(nullable=false, length = 150)
    private String calle;
    @Column(nullable = false, length = 50)
    private String numero;
    @Column(nullable = false,length = 100)
    private String ciudad;
    @Column(name = "codigo_postal", nullable = false, length = 50)
    private String codigoPostal;
    @Column(nullable = false)
    private Boolean predeterminado;


    public Direccion(){};

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
    public String getCalle(){
        return calle;
    }
    public void setCalle(String calle){
        this.calle = calle;
    }

    public String getNumero(){
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getCiudad(){
        return ciudad;
    }
    public void setCiudad(String ciudad){
        this.ciudad = ciudad;
    }
    
    public String getCodigoPostal(){
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal){
        this.codigoPostal = codigoPostal;
    }
    
    public Boolean getPredeterminado() {
        return predeterminado;
    }

    public void setPredeterminado(Boolean predeterminado) {
        this.predeterminado = predeterminado;
    }


}