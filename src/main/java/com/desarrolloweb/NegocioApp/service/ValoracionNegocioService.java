package com.desarrolloweb.NegocioApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.desarrolloweb.NegocioApp.entity.ValoracionNegocio;
import com.desarrolloweb.NegocioApp.repository.ValoracionNegocioRepository;

@Service
public class ValoracionNegocioService {

	private final ValoracionNegocioRepository valoracionRepository;

	public ValoracionNegocioService(ValoracionNegocioRepository valoracionRepository) {
		this.valoracionRepository = valoracionRepository;
	}

	public List<ValoracionNegocio> obtenerTodas() {
		return valoracionRepository.findAll();
	}

	public Optional<ValoracionNegocio> obtenerPorId(Long id) {
		return valoracionRepository.findById(id);
	}

	public ValoracionNegocio crear(ValoracionNegocio valoracion) {
		if (valoracion.getFecha() == null) {
			valoracion.setFecha(LocalDateTime.now());
		}
		return valoracionRepository.save(valoracion);
	}

	public Optional<ValoracionNegocio> actualizar(Long id, ValoracionNegocio actualizada) {
		return valoracionRepository.findById(id)
				.map(valoracion -> {
					if (actualizada.getEstrellas() != null) {
						valoracion.setEstrellas(actualizada.getEstrellas());
					}
					if (actualizada.getComentario() != null) {
						valoracion.setComentario(actualizada.getComentario());
					}
					return valoracionRepository.save(valoracion);
				});
	}

	public boolean existe(Long id) {
		return valoracionRepository.existsById(id);
	}

	public void eliminar(Long id) {
		valoracionRepository.deleteById(id);
	}

	public List<ValoracionNegocio> obtenerPorNegocio(Long negocioId) {
		return valoracionRepository.findByNegocioId(negocioId);
	}

	public List<ValoracionNegocio> obtenerPorCliente(Long clienteId) {
		return valoracionRepository.findByClienteId(clienteId);
	}

	public List<ValoracionNegocio> obtenerPorEstrellas(Integer estrellas) {
		return valoracionRepository.findByEstrellas(estrellas);
	}
}
