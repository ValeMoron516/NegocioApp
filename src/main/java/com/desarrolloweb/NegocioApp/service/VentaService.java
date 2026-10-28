
package com.desarrolloweb.NegocioApp.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.desarrolloweb.NegocioApp.repository.VentaRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.DireccionRepository;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.DetalleVentaRepository;

import com.desarrolloweb.NegocioApp.dto.VentaRequestDto;
import com.desarrolloweb.NegocioApp.dto.VentaResponseDto;

import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Venta;
import com.desarrolloweb.NegocioApp.entity.Direccion;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.DetalleVenta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final NegocioRepository negocioRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @Autowired
    public VentaService(
            VentaRepository ventaRepository,
            UsuarioRepository usuarioRepository,
            DireccionRepository direccionRepository,
            NegocioRepository negocioRepository,
            DetalleVentaRepository detalleVentaRepository) {

        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.negocioRepository = negocioRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    // CREAR VENTA
    public VentaResponseDto crear(VentaRequestDto request) {

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        Direccion direccion = direccionRepository
                .findById(request.getDireccionId())
                .orElseThrow(() ->
                        new RuntimeException("Direccion no encontrada"));

        Negocio negocio = negocioRepository
                .findById(request.getNegocioId())
                .orElseThrow(() ->
                        new RuntimeException("Negocio no encontrado"));

        Venta venta = new Venta();

        venta.setUsuario(usuario);
        venta.setDireccion(direccion);
        venta.setNegocio(negocio);
        venta.setFechaVenta(request.getFechaVenta().atStartOfDay());
        venta.setEstado(request.getEstado());
        venta.setTotal(request.getTotal());

        Venta guardada = ventaRepository.save(venta);

        return convertirAResponse(guardada);
    }

    // BUSCAR VENTA POR ID, INCLUYENDO SUS PRODUCTOS
    @Transactional(readOnly = true)
    public VentaResponseDto buscarPorId(Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Venta no encontrada"));

        VentaResponseDto respuesta = convertirAResponse(venta);

        List<DetalleVenta> detalles =
                detalleVentaRepository.findByVentaId(id);

        List<VentaResponseDto.ProductoVendidoDto> productos =
                new ArrayList<>();

        for (DetalleVenta detalle : detalles) {

            productos.add(
                    new VentaResponseDto.ProductoVendidoDto(
                            detalle.getProducto().getId(),
                            detalle.getProducto().getNombre(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario()
                    )
            );
        }

        respuesta.setProductos(productos);

        return respuesta;
    }

    // BUSCAR VENTAS POR FECHAS
    public List<VentaResponseDto> buscarPorFechas(
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        LocalDateTime desde = fechaDesde.atStartOfDay();
        LocalDateTime hasta = fechaHasta.atTime(23, 59, 59);

        List<Venta> ventas =
                ventaRepository.findByFechaVentaBetween(desde, hasta);

        List<VentaResponseDto> resultado = new ArrayList<>();

        for (Venta venta : ventas) {
            resultado.add(convertirAResponse(venta));
        }

        return resultado;
    }

    // BUSCAR VENTAS POR USUARIO
    public List<VentaResponseDto> buscarPorUsuario(Long usuarioId) {

        List<Venta> ventas =
                ventaRepository.findByUsuarioId(usuarioId);

        List<VentaResponseDto> resultado = new ArrayList<>();

        for (Venta venta : ventas) {
            resultado.add(convertirAResponse(venta));
        }

        return resultado;
    }

    // LISTAR TODAS LAS VENTAS
    public List<VentaResponseDto> listarTodo() {

        List<Venta> ventas = ventaRepository.findAll();

        List<VentaResponseDto> resultado = new ArrayList<>();

        for (Venta venta : ventas) {
            resultado.add(convertirAResponse(venta));
        }

        return resultado;
    }

    // ACTUALIZAR VENTA
    public VentaResponseDto actualizar(
            Long id,
            VentaRequestDto request) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Venta no encontrada"));

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        Direccion direccion = direccionRepository
                .findById(request.getDireccionId())
                .orElseThrow(() ->
                        new RuntimeException("Direccion no encontrada"));

        Negocio negocio = negocioRepository
                .findById(request.getNegocioId())
                .orElseThrow(() ->
                        new RuntimeException("Negocio no encontrado"));

        venta.setUsuario(usuario);
        venta.setDireccion(direccion);
        venta.setNegocio(negocio);
        venta.setFechaVenta(request.getFechaVenta().atStartOfDay());
        venta.setEstado(request.getEstado());
        venta.setTotal(request.getTotal());

        Venta actualizada = ventaRepository.save(venta);

        return convertirAResponse(actualizada);
    }

    // ELIMINAR VENTA
    public void eliminar(Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Venta no encontrada"));

        ventaRepository.delete(venta);
    }

    // CONTAR VENTAS POR NEGOCIO
    public Long contarPorNegocio(Long negocioId) {

        if (!negocioRepository.existsById(negocioId)) {
            throw new RuntimeException("Negocio no encontrado");
        }

        return ventaRepository.countByNegocioId(negocioId);
    }

    // CONVERTIR ENTIDAD VENTA A DTO
    private VentaResponseDto convertirAResponse(Venta venta) {

        return new VentaResponseDto(
                venta.getId(),
                venta.getUsuario().getId(),
                venta.getDireccion().getId(),
                venta.getNegocio().getId(),
                venta.getFechaVenta().toLocalDate(),
                venta.getEstado(),
                venta.getTotal()
        );
    }
}
