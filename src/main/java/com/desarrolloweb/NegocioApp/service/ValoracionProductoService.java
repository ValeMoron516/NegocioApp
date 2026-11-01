package com.desarrolloweb.NegocioApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrolloweb.NegocioApp.dto.ValoracionProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoResponseDTO;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoPromedioDTO;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.ValoracionProducto;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.ValoracionProductoRepository;

@Service
public class ValoracionProductoService {
    private final ValoracionProductoRepository valoracionProductoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public ValoracionProductoService(ValoracionProductoRepository valoracionProductoRepository,
            UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.valoracionProductoRepository = valoracionProductoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public ValoracionProductoResponseDTO crearValoracion(ValoracionProductoRequestDTO dto) {
        if (dto == null || dto.getUsuarioId() == null || dto.getProductoId() == null) {
            throw new IllegalArgumentException("usuarioId y productoId son obligatorios");
        }
        if (dto.getEstrellas() < 1 || dto.getEstrellas() > 5) {
            throw new IllegalArgumentException("La puntuacion debe estar entre 1 y 5");
        }
        if (dto.getComentario() != null && dto.getComentario().length() > 500) {
            throw new IllegalArgumentException("El comentario supera los 500 caracteres");
        }
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));

        ValoracionProducto valoracion = new ValoracionProducto();
        valoracion.setUsuarioId(usuario);
        valoracion.setProductoId(producto);
        valoracion.setEstrellas(dto.getEstrellas());
        valoracion.setComentario(dto.getComentario());
        valoracion.setFecha(LocalDateTime.now());
        return convertir(valoracionProductoRepository.save(valoracion));
    }

    @Transactional(readOnly = true)
    public List<ValoracionProductoResponseDTO> listarTodasLasValoraciones() {
        return valoracionProductoRepository.findAll().stream().map(this::convertir).toList();
    }

    @Transactional(readOnly = true)
    public List<ValoracionProductoResponseDTO> obtenerPorProducto(Long productoId) {
        return valoracionProductoRepository.findByProductoId_Id(productoId).stream()
                .map(this::convertir).toList();
    }

    @Transactional(readOnly = true)
    public ValoracionProductoPromedioDTO obtenerPromedio(Long productoId) {
        List<ValoracionProducto> lista = valoracionProductoRepository.findByProductoId_Id(productoId);
        if (lista.isEmpty()) return new ValoracionProductoPromedioDTO(productoId, 0.0, 0);
        double promedio = lista.stream().mapToInt(ValoracionProducto::getEstrellas).average().orElse(0.0);
        return new ValoracionProductoPromedioDTO(productoId, promedio, lista.size());
    }

    @Transactional
    public void eliminarValoracion(Long id) {
        if (!valoracionProductoRepository.existsById(id)) {
            throw new NoSuchElementException("No existe una valoracion con el ID: " + id);
        }
        valoracionProductoRepository.deleteById(id);
    }

    private ValoracionProductoResponseDTO convertir(ValoracionProducto valoracion) {
        return new ValoracionProductoResponseDTO(
                valoracion.getId(),
                valoracion.getUsuarioId() == null ? null : valoracion.getUsuarioId().getId(),
                valoracion.getProductoId() == null ? null : valoracion.getProductoId().getId(),
                valoracion.getEstrellas(), valoracion.getComentario(),
                valoracion.getFecha() == null ? null : valoracion.getFecha().toString());
    }
}
