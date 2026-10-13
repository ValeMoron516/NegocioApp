package com.desarrolloweb.NegocioApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.desarrolloweb.NegocioApp.entity.DetalleVenta;
import com.desarrolloweb.NegocioApp.repository.DetalleVentaRepository;

@Service
public class DetalleVentaService {

	private final DetalleVentaRepository detalleVentaRepository;

	public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
		this.detalleVentaRepository = detalleVentaRepository;
	}

	public List<DetalleVenta> obtenerTodos() {
		return detalleVentaRepository.findAll();
	}

	public Optional<DetalleVenta> obtenerPorId(Long id) {
		return detalleVentaRepository.findById(id);
	}

	public DetalleVenta crear(DetalleVenta detalleVenta) {
		return detalleVentaRepository.save(detalleVenta);
	}

	public Optional<DetalleVenta> actualizarCantidad(Long id, Integer cantidad) {
		return detalleVentaRepository.findById(id)
				.map(detalle -> {
					if (cantidad != null) {
						detalle.setCantidad(cantidad);
					}
					return detalleVentaRepository.save(detalle);
				});
	}

	public boolean existe(Long id) {
		return detalleVentaRepository.existsById(id);
	}

	public void eliminar(Long id) {
		detalleVentaRepository.deleteById(id);
	}

	public List<DetalleVenta> obtenerPorVenta(Long ventaId) {
		return detalleVentaRepository.findByVentaId(ventaId);
	}

	public List<DetalleVenta> obtenerPorProducto(Long productoId) {
		return detalleVentaRepository.findByProductoId(productoId);
	}
}
