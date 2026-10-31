

package com.desarrolloweb.NegocioApp.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.desarrolloweb.NegocioApp.repository.VentaRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.DireccionRepository;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.DetalleVentaRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;

import com.desarrolloweb.NegocioApp.dto.VentaRequestDto;
import com.desarrolloweb.NegocioApp.dto.VentaResponseDto;

import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Venta;
import com.desarrolloweb.NegocioApp.entity.Direccion;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.DetalleVenta;
import com.desarrolloweb.NegocioApp.entity.Producto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final NegocioRepository negocioRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public VentaService(
            VentaRepository ventaRepository,
            UsuarioRepository usuarioRepository,
            DireccionRepository direccionRepository,
            NegocioRepository negocioRepository,
            DetalleVentaRepository detalleVentaRepository,
            ProductoRepository productoRepository) {

        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.negocioRepository = negocioRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
    }

    // CREAR VENTA CON PRODUCTOS
    @Transactional
    public VentaResponseDto crear(VentaRequestDto request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Los datos de la venta son obligatorios");
        }

        if (request.getUsuarioId() == null
                || request.getDireccionId() == null
                || request.getNegocioId() == null) {
            throw new IllegalArgumentException(
                    "Usuario, direccion y negocio son obligatorios");
        }

        if (request.getProductos() == null
                || request.getProductos().isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe incluir al menos un producto");
        }

        // Validar los productos antes de guardar la venta
        Set<Long> productosProcesados = new HashSet<>();

        for (VentaRequestDto.ProductoCompraDto item :
                request.getProductos()) {

            if (item == null || item.productoId() == null) {
                throw new IllegalArgumentException(
                        "El ID del producto es obligatorio");
            }

            if (item.cantidad() == null || item.cantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero");
            }

            if (!productosProcesados.add(item.productoId())) {
                throw new IllegalArgumentException(
                        "No se puede repetir el mismo producto");
            }
        }

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

        // Ordenar los productos para adquirir los bloqueos
        // en un orden consistente
        List<VentaRequestDto.ProductoCompraDto> items =
                new ArrayList<>(request.getProductos());

        items.sort(
                Comparator.comparing(
                        VentaRequestDto.ProductoCompraDto::productoId)
        );

        BigDecimal total = BigDecimal.ZERO;

        List<Producto> productosEncontrados = new ArrayList<>();

        // BUSCAR PRODUCTOS Y BLOQUEARLOS PARA LA VENTA
        for (VentaRequestDto.ProductoCompraDto item : items) {

            Producto producto = productoRepository
                    .buscarParaVenta(item.productoId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Producto no encontrado: "
                                            + item.productoId()));

            // Verificar que pertenece al negocio
            if (!producto.getNegocio().getId()
                    .equals(negocio.getId())) {
                throw new IllegalArgumentException(
                        "El producto no pertenece al negocio");
            }

            // Validar el precio
            if (producto.getPrecio() == null
                    || producto.getPrecio().signum() < 0) {
                throw new IllegalArgumentException(
                        "El producto tiene un precio invalido");
            }

            // Verificar el stock
            if (producto.getStock() == null
                    || producto.getStock() < item.cantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para: "
                                + producto.getNombre());
            }

            // Calcular el subtotal
            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.cantidad()));

            total = total.add(subtotal);

            productosEncontrados.add(producto);
        }

        // CREAR LA VENTA
        Venta venta = new Venta();

        venta.setUsuario(usuario);
        venta.setDireccion(direccion);
        venta.setNegocio(negocio);

        LocalDate fecha = request.getFechaVenta() != null
                ? request.getFechaVenta()
                : LocalDate.now();

        venta.setFechaVenta(fecha.atStartOfDay());

        venta.setEstado(
                request.getEstado() != null
                        && !request.getEstado().isBlank()
                        ? request.getEstado()
                        : "PENDIENTE"
        );

        // El total se calcula en el servidor
        venta.setTotal(total);

        Venta guardada = ventaRepository.save(venta);

        List<DetalleVenta> detalles = new ArrayList<>();

        // GUARDAR DETALLES Y DESCONTAR STOCK
        for (int i = 0; i < items.size(); i++) {

            VentaRequestDto.ProductoCompraDto item = items.get(i);
            Producto producto = productosEncontrados.get(i);

            DetalleVenta detalle = new DetalleVenta();

            detalle.setVenta(guardada);
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            detalles.add(detalle);

            producto.setStock(
                    producto.getStock() - item.cantidad());

            productoRepository.save(producto);
        }

        detalleVentaRepository.saveAll(detalles);

        // PREPARAR RESPUESTA CON PRODUCTOS
        VentaResponseDto respuesta = convertirAResponse(guardada);

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

    // BUSCAR VENTA POR ID CON PRODUCTOS
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
        LocalDateTime hasta = fechaHasta.plusDays(1).atStartOfDay();

        List<Venta> ventas =
                ventaRepository.findByFechaVentaBetween(
                        desde, hasta.minusNanos(1));

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

    // CONVERTIR ENTIDAD A DTO
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
