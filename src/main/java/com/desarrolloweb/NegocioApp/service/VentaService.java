
package com.desarrolloweb.NegocioApp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Locale;

import org.springframework.stereotype.Service;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import org.springframework.transaction.annotation.Transactional;

import com.desarrolloweb.NegocioApp.dto.venta.VentaRequestDTO;
import com.desarrolloweb.NegocioApp.dto.venta.VentaResponseDTO;

import com.desarrolloweb.NegocioApp.entity.DetalleVenta;
import com.desarrolloweb.NegocioApp.entity.Direccion;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Venta;

import com.desarrolloweb.NegocioApp.repository.DetalleVentaRepository;
import com.desarrolloweb.NegocioApp.repository.DireccionRepository;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.VentaRepository;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final NegocioRepository negocioRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

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
    public VentaResponseDTO crear(VentaRequestDTO request) {

        if (request == null
                || request.getUsuarioId() == null
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

        if (request.getEstado() != null
                && !"PENDIENTE".equalsIgnoreCase(request.getEstado().trim())) {

            throw new IllegalArgumentException(
                    "Las ventas nuevas deben comenzar PENDIENTES");
        }

        Set<Long> ids = new HashSet<>();

        for (VentaRequestDTO.ProductoCompraDTO item :
                request.getProductos()) {

            if (item == null
                    || item.productoId() == null
                    || item.cantidad() == null
                    || item.cantidad() <= 0) {

                throw new IllegalArgumentException(
                        "Cada producto requiere ID y cantidad mayor que cero");
            }

            if (!ids.add(item.productoId())) {
                throw new IllegalArgumentException(
                        "No se puede repetir el mismo producto");
            }
        }

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Usuario no encontrado"));

        Direccion direccion = direccionRepository
                .findById(request.getDireccionId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Direccion no encontrada"));

        Negocio negocio = negocioRepository
                .findById(request.getNegocioId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Negocio no encontrado"));

        if (!negocio.estaAprobado()) {
            throw new ConflictException("El negocio aun no esta aprobado para vender");
        }
        if (!request.getUsuarioId().equals(direccion.getUsuarioId())) {
            throw new IllegalArgumentException(
                    "La direccion no pertenece al comprador");
        }

        // Ordenar los productos para evitar bloqueos cruzados
        List<VentaRequestDTO.ProductoCompraDTO> items =
                new ArrayList<>(request.getProductos());

        items.sort(Comparator.comparing(
                VentaRequestDTO.ProductoCompraDTO::productoId));

        List<Producto> productos = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        // COMPROBAR PRODUCTOS Y STOCK
        for (VentaRequestDTO.ProductoCompraDTO item : items) {

            Producto producto = productoRepository
                    .buscarParaVenta(item.productoId())
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "Producto no encontrado: "
                                            + item.productoId()));

            if (!producto.getNegocio().getId()
                    .equals(negocio.getId())) {

                throw new IllegalArgumentException(
                        "El producto no pertenece al negocio");
            }

            if (producto.getPrecio() == null
                    || producto.getPrecio().signum() < 0) {

                throw new IllegalArgumentException(
                        "El producto tiene un precio invalido");
            }

            if (producto.getStock() == null
                    || producto.getStock() < item.cantidad()) {

                throw new IllegalArgumentException(
                        "Stock insuficiente para: "
                                + producto.getNombre());
            }

            total = total.add(
                    producto.getPrecio().multiply(
                            BigDecimal.valueOf(item.cantidad()))
            );

            productos.add(producto);
        }

        // GUARDAR VENTA
        Venta venta = new Venta();

        venta.setUsuario(usuario);
        venta.setDireccion(direccion);
        venta.setNegocio(negocio);

        LocalDate fecha = request.getFechaVenta() == null
                ? LocalDate.now()
                : request.getFechaVenta();

        venta.setFechaVenta(fecha.atStartOfDay());
        venta.setEstado("PENDIENTE");
        venta.setTotal(total);

        Venta guardada = ventaRepository.save(venta);

        List<DetalleVenta> detalles = new ArrayList<>();

        // GUARDAR DETALLES Y DESCONTAR STOCK
        for (int i = 0; i < items.size(); i++) {

            VentaRequestDTO.ProductoCompraDTO item = items.get(i);
            Producto producto = productos.get(i);

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

        return convertirConDetalles(guardada, detalles);
    }

    // BUSCAR VENTA POR ID
    @Transactional(readOnly = true)
    public VentaResponseDTO buscarPorId(Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Venta no encontrada"));

        return convertirConDetalles(
                venta,
                detalleVentaRepository.findByVentaId(id)
        );
    }

    // BUSCAR VENTAS POR FECHAS
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> buscarPorFechas(
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        if (fechaDesde == null
                || fechaHasta == null
                || fechaDesde.isAfter(fechaHasta)) {

            throw new IllegalArgumentException(
                    "Rango de fechas invalido");
        }

        LocalDateTime desde = fechaDesde.atStartOfDay();

        LocalDateTime hasta = fechaHasta
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        return convertirLista(
                ventaRepository.findByFechaVentaBetween(desde, hasta)
        );
    }

    // BUSCAR VENTAS POR USUARIO
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> buscarPorUsuario(Long usuarioId) {

        return convertirLista(
                ventaRepository.findByUsuarioId(usuarioId)
        );
    }

    // LISTAR VENTAS DE UN COMERCIO (la autorizacion ocurre en el controlador)
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarPorNegocio(Long negocioId) {
        return convertirLista(ventaRepository.findByNegocioId(negocioId));
    }

    // LISTAR TODAS LAS VENTAS
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarTodo() {

        return convertirLista(ventaRepository.findAll());
    }

    // ACTUALIZAR METADATOS DE LA VENTA
    @Transactional
    public VentaResponseDTO actualizar(
            Long id,
            VentaRequestDTO request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Datos de la venta obligatorios");
        }

        Venta venta = ventaRepository.buscarParaModificar(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Venta no encontrada"));

        if (request.getUsuarioId() != null
                && !request.getUsuarioId()
                        .equals(venta.getUsuario().getId())) {

            throw new IllegalArgumentException(
                    "No se puede cambiar el comprador de una venta");
        }

        if (request.getNegocioId() != null
                && !request.getNegocioId()
                        .equals(venta.getNegocio().getId())) {

            throw new IllegalArgumentException(
                    "No se puede cambiar el negocio de una venta");
        }

        if (request.getProductos() != null
                && !request.getProductos().isEmpty()) {

            throw new IllegalArgumentException(
                    "Modifica los productos usando los endpoints de detalles");
        }

        if (request.getTotal() != null
                && request.getTotal().compareTo(venta.getTotal()) != 0) {

            throw new IllegalArgumentException(
                    "El total se calcula desde los detalles y no puede editarse");
        }

        if (request.getDireccionId() != null) {

            Direccion direccion = direccionRepository
                    .findById(request.getDireccionId())
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "Direccion no encontrada"));

            if (!direccion.getUsuarioId()
                    .equals(venta.getUsuario().getId())) {

                throw new IllegalArgumentException(
                        "La direccion no pertenece al comprador");
            }

            venta.setDireccion(direccion);
        }

        if (request.getFechaVenta() != null) {
            venta.setFechaVenta(
                    request.getFechaVenta().atStartOfDay());
        }

        if (request.getEstado() != null
                && !request.getEstado().isBlank()) {

            String nuevo = normalizarEstado(request.getEstado());

            if (!Set.of(
                    "PENDIENTE",
                    "PAGADO",
                    "ENVIADO",
                    "ENTREGADO",
                    "CANCELADO"
            ).contains(nuevo)) {

                throw new IllegalArgumentException(
                        "Estado de venta desconocido");
            }

            String actual = normalizarEstado(venta.getEstado());
            validarCambioEstado(actual, nuevo);

            if ("CANCELADO".equals(nuevo)
                    && !"CANCELADO".equals(actual)) {

                reponerStock(
                        detalleVentaRepository.findByVentaId(id)
                );
            }

            venta.setEstado(nuevo);
        }

        Venta actualizada = ventaRepository.save(venta);

        return convertirConDetalles(
                actualizada,
                detalleVentaRepository.findByVentaId(id)
        );
    }

    // Cambio controlado por un endpoint exclusivo. No permite editar importes ni compradores.
    @Transactional
    public VentaResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank())
            throw new IllegalArgumentException("El estado es obligatorio");
        VentaRequestDTO dto = new VentaRequestDTO();
        dto.setEstado(nuevoEstado);
        return actualizar(id, dto);
    }

    // ELIMINAR VENTA
    // Acepta estados históricos en mayúsculas o minúsculas
    @Transactional
    public void eliminar(Long id) {

        Venta venta = ventaRepository.buscarParaModificar(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Venta no encontrada"));

        String estado = normalizarEstado(venta.getEstado());

        if (!"PENDIENTE".equals(estado)
                && !"CANCELADO".equals(estado)) {

            throw new ConflictException(
                    "No se pueden eliminar ventas pagadas, enviadas o entregadas");
        }

        List<DetalleVenta> detalles =
                detalleVentaRepository.findByVentaId(id);

        // Reponer el inventario solo si estaba pendiente
        if ("PENDIENTE".equals(estado)) {
            reponerStock(detalles);
        }

        detalleVentaRepository.deleteAll(detalles);
        ventaRepository.delete(venta);
    }

    // CONTAR VENTAS POR NEGOCIO
    public Long contarPorNegocio(Long negocioId) {

        if (!negocioRepository.existsById(negocioId)) {
            throw new NoSuchElementException(
                    "Negocio no encontrado");
        }

        return ventaRepository.countByNegocioId(negocioId);
    }

    // Normalizar registros históricos sin alterar los demás campos de la venta.
    private String normalizarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new ConflictException("La venta no tiene un estado valido");
        }
        return estado.trim().toUpperCase(Locale.ROOT);
    }

    // VALIDAR CAMBIO DE ESTADO
    private void validarCambioEstado(
            String actual,
            String nuevo) {

        if (actual.equals(nuevo)) {
            return;
        }

        boolean permitido = switch (actual) {

            case "PENDIENTE" ->
                    "PAGADO".equals(nuevo)
                            || "CANCELADO".equals(nuevo);

            case "PAGADO" ->
                    "ENVIADO".equals(nuevo)
                            || "CANCELADO".equals(nuevo);

            case "ENVIADO" ->
                    "ENTREGADO".equals(nuevo);

            default -> false;
        };

        if (!permitido) {
            throw new ConflictException(
                    "Cambio de estado no permitido: "
                            + actual + " a " + nuevo);
        }
    }

    // REPONER STOCK
    private void reponerStock(List<DetalleVenta> detalles) {

        List<DetalleVenta> ordenados =
                new ArrayList<>(detalles);

        ordenados.sort(
                Comparator.comparing(
                        d -> d.getProducto().getId())
        );

        for (DetalleVenta detalle : ordenados) {

            Producto producto = productoRepository
                    .buscarParaVenta(detalle.getProducto().getId())
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "Producto no encontrado"));

            producto.setStock(
                    Math.addExact(
                            producto.getStock(),
                            detalle.getCantidad())
            );

            productoRepository.save(producto);
        }
    }

    // CONVERTIR LISTA DE VENTAS A DTO
    private List<VentaResponseDTO> convertirLista(
            List<Venta> ventas) {

        List<VentaResponseDTO> resultado =
                new ArrayList<>();

        for (Venta venta : ventas) {

            resultado.add(
                    convertirConDetalles(
                            venta,
                            detalleVentaRepository.findByVentaId(
                                    venta.getId())
                    )
            );
        }

        return resultado;
    }

    // CONVERTIR VENTA Y DETALLES A DTO
    private VentaResponseDTO convertirConDetalles(
            Venta venta,
            List<DetalleVenta> detalles) {

        VentaResponseDTO dto = new VentaResponseDTO(
                venta.getId(),
                venta.getUsuario().getId(),
                venta.getDireccion().getId(),
                venta.getNegocio().getId(),
                venta.getFechaVenta().toLocalDate(),
                venta.getEstado(),
                venta.getTotal()
        );

        List<VentaResponseDTO.ProductoVendidoDto> productos =
                new ArrayList<>();

        for (DetalleVenta detalle : detalles) {

            productos.add(
                    new VentaResponseDTO.ProductoVendidoDto(
                            detalle.getProducto().getId(),
                            detalle.getProducto().getNombre(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario()
                    )
            );
        }

        dto.setProductos(productos);

        return dto;
    }
}
