package com.desarrolloweb.NegocioApp.dtos.ValoracionNegocioDTO;

public class ValoracionNegocioRequest {

	private Long negocioId;
	private Long clienteId;
	private Integer estrellas;
	private String comentario;

	public ValoracionNegocioRequest() {
	}

	public ValoracionNegocioRequest(Long negocioId, Long clienteId,
			Integer estrellas, String comentario) {
		this.negocioId = negocioId;
		this.clienteId = clienteId;
		this.estrellas = estrellas;
		this.comentario = comentario;
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
}
