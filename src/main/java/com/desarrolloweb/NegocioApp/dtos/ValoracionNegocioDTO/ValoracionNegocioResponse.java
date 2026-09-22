package com.desarrolloweb.NegocioApp.dtos.ValoracionNegocioDTO;

import java.time.LocalDateTime;

public class ValoracionNegocioResponse {

	private Long id;
	private Long negocioId;
	private Long clienteId;
	private Integer estrellas;
	private String comentario;
	private LocalDateTime fecha;

	public ValoracionNegocioResponse() {
	}

	public ValoracionNegocioResponse(Long id, Long negocioId, Long clienteId,
			Integer estrellas, String comentario, LocalDateTime fecha) {
		this.id = id;
		this.negocioId = negocioId;
		this.clienteId = clienteId;
		this.estrellas = estrellas;
		this.comentario = comentario;
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNegocioId() {
		return negocioId;
	}

	public void setNegocioId(Long negocioId) {
		this.negocioId = negocioId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public Integer getEstrellas() {
		return estrellas;
	}

	public void setEstrellas(Integer estrellas) {
		this.estrellas = estrellas;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
}
