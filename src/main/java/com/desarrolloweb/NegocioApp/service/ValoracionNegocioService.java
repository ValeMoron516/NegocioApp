package com.desarrolloweb.NegocioApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.desarrolloweb.NegocioApp.entity.ValoracionNegocio;
import com.desarrolloweb.NegocioApp.dto.valoracionNegocio.ValoracionNegocioRequestDTO;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.repository.ValoracionNegocioRepository;

@Service
public class ValoracionNegocioService {

	private final ValoracionNegocioRepository valoracionRepository;
    private final com.desarrolloweb.NegocioApp.repository.NegocioRepository negocios;
    private final com.desarrolloweb.NegocioApp.repository.UsuarioRepository usuarios;

	public ValoracionNegocioService(ValoracionNegocioRepository valoracionRepository,
             com.desarrolloweb.NegocioApp.repository.NegocioRepository negocios,
             com.desarrolloweb.NegocioApp.repository.UsuarioRepository usuarios) {
        this.negocios = negocios;
        this.usuarios = usuarios;
		this.valoracionRepository = valoracionRepository;
	}

	public List<ValoracionNegocio> obtenerTodas() {
		return valoracionRepository.findAll();
	}

	public Optional<ValoracionNegocio> obtenerPorId(Long id) {
		return valoracionRepository.findById(id);
	}

    @org.springframework.transaction.annotation.Transactional
    public ValoracionNegocio crear(ValoracionNegocioRequestDTO request, Long usuarioAutenticadoId) {
        if (request == null || request.getNegocioId() == null || usuarioAutenticadoId == null) {
            throw new IllegalArgumentException("negocioId y cliente autenticado son obligatorios");
        }
        validar(request.getEstrellas(), request.getComentario());
        ValoracionNegocio nueva = new ValoracionNegocio();
        nueva.setNegocio(negocios.findById(request.getNegocioId())
                .orElseThrow(() -> new NotFoundException("Negocio no encontrado")));
        nueva.setCliente(usuarios.findById(usuarioAutenticadoId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado")));
        nueva.setEstrellas(request.getEstrellas());
        nueva.setComentario(request.getComentario());
        nueva.setFecha(LocalDateTime.now());
        return valoracionRepository.save(nueva);
    }

    @org.springframework.transaction.annotation.Transactional
    public Optional<ValoracionNegocio> actualizar(Long id, ValoracionNegocioRequestDTO actualizada) {
        if (actualizada == null) throw new IllegalArgumentException("Valoracion obligatoria");
        return valoracionRepository.findById(id).map(valoracion -> {
            if (actualizada.getEstrellas() != null) valoracion.setEstrellas(actualizada.getEstrellas());
            if (actualizada.getComentario() != null) valoracion.setComentario(actualizada.getComentario());
            validar(valoracion.getEstrellas(), valoracion.getComentario());
            return valoracionRepository.save(valoracion);
        });
    }

    private void validar(Integer estrellas, String comentario) {
        if (estrellas == null || estrellas < 1 || estrellas > 5)
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5");
        if (comentario != null && comentario.length() > 500)
            throw new IllegalArgumentException("El comentario supera 500 caracteres");
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
