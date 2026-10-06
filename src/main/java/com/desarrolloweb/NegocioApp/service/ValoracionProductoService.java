
package com.desarrolloweb.NegocioApp.service;

import org.springframework.stereotype.Service;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoResponseDTO;
import com.desarrolloweb.NegocioApp.repository.ValoracionProductoRepository;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.entity.ValoracionProducto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import com.desarrolloweb.NegocioApp.dto.ValoracionProductoPromedioDTO;

@Service
public class ValoracionProductoService {

    private final ValoracionProductoRepository valoracionProductoRepository;

    public ValoracionProductoService(ValoracionProductoRepository valoracionProductoRepository) {
        this.valoracionProductoRepository = valoracionProductoRepository;
    }

    public ValoracionProductoResponseDTO crearValoracion(ValoracionProductoRequestDTO dto) {
        // 1. Validar la puntuación (1 a 5)
        if (dto.getPuntuacion() < 1 || dto.getPuntuacion() > 5) {
            throw new IllegalArgumentException("La puntuación debe ser un número entero entre 1 y 5.");
        }

        // 2. Instanciar la entidad
        ValoracionProducto valoracion = new ValoracionProducto();
        valoracion.setPuntuacion(dto.getPuntuacion());
        valoracion.setComentario(dto.getComentario());
        valoracion.setFecha(LocalDateTime.now());

        // 3. Setear Producto (y dejamos Usuario temporalmente en null)
        Producto producto = new Producto();
        producto.setId(dto.getProductoId());
        valoracion.setProductoId(producto);

        // 4. Guardar en la base de datos
        ValoracionProducto guardada = valoracionProductoRepository.save(valoracion);

        // 5. Retornar el ResponseDTO
        return new ValoracionProductoResponseDTO(
            guardada.getId(),
            10L, // ID de usuario simulado para la respuesta
            guardada.getProductoId().getId(),
            guardada.getPuntuacion(),
            guardada.getComentario(),
            guardada.getFecha().toString()
        );
    }

   public List<ValoracionProductoResponseDTO> obtenerPorProducto (Long productoId){

        //1. Se busca todas las valoraciones del producto en la base de datos
    List<ValoracionProducto> listaEntidades = valoracionProductoRepository.findByProductoId_Id(productoId);
        //2. Preparar una lista vacia de DTOs
    List<ValoracionProductoResponseDTO> lisDTOs = new ArrayList<>();
        //3. Recorrer cada entidad y transformarlas en DTO
    for (ValoracionProducto valoracion: listaEntidades){
        ValoracionProductoResponseDTO dto = new ValoracionProductoResponseDTO(
            valoracion.getId(),
            10L,
            productoId,
            valoracion.getPuntuacion(),
            valoracion.getComentario(),
            valoracion.getFecha() != null ? valoracion.getFecha().toString() : null
        );
        lisDTOs.add(dto);
    }
      return lisDTOs;

    }

   public ValoracionProductoPromedioDTO obtenerPromedio(Long productoId) {

    List<ValoracionProducto> listaEntidades = valoracionProductoRepository.findByProductoId_Id(productoId);

    if (listaEntidades.isEmpty()) {
        return new ValoracionProductoPromedioDTO(productoId, 0.0, 0);
    }

    double suma = 0;
    for (ValoracionProducto valoracion : listaEntidades) {
        suma += valoracion.getPuntuacion();
    }

    double promedio = suma / listaEntidades.size();
    return new ValoracionProductoPromedioDTO(productoId, promedio, listaEntidades.size());
}

   public void eliminarValoracion(Long id) {
    if (!valoracionProductoRepository.existsById(id)) {
        throw new IllegalArgumentException("No existe una valoración con el ID: " + id);
    }
    valoracionProductoRepository.deleteById(id);
}
}