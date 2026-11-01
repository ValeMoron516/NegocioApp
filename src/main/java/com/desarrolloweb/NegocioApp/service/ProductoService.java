package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoResponseDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;
import com.desarrolloweb.NegocioApp.repository.DetalleVentaRepository;
import com.desarrolloweb.NegocioApp.repository.ValoracionProductoRepository;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductoService {

    @Autowired private ProductoRepository productoRepository;
    @Autowired private NegocioRepository negocioRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private DetalleVentaRepository detalleVentaRepository;
    @Autowired private ValoracionProductoRepository valoracionProductoRepository;

    @Transactional(readOnly = true)
    public PaginacionDTO<ProductoResponseDTO> obtenerTodosProductos(Integer page, Integer limit) {
        if (page == null || page < 1 || limit == null || limit < 1 || limit > 100) {
            throw new BadRequestException("Pagina o limite invalido (limite de 1 a 100)");
        }
        Page<Producto> pagina = productoRepository.findAll(PageRequest.of(page - 1, limit));
        List<ProductoResponseDTO> productos = pagina.getContent().stream().map(this::convertir).toList();
        MetaDTO meta = new MetaDTO(
                pagina.getTotalElements(), pagina.getNumberOfElements(), pagina.getSize(),
                pagina.getTotalPages(), pagina.getNumber() + 1);
        return new PaginacionDTO<>(productos, meta);
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        return convertir(productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El elemento solicitado no existe")));
    }

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        validarDatos(dto, true);
        Negocio negocio = negocioRepository.findById(dto.getNegocioId())
                .orElseThrow(() -> new NotFoundException("El negocio solicitado no existe"));
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("La categoria solicitada no existe"));

        Producto producto = new Producto();
        producto.setNegocio(negocio);
        producto.setCategoria(categoria);
        producto.setNombre(dto.getNombre().trim());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(precioValido(dto.getPrecio()));
        producto.setStock(dto.getStock());
        return convertir(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponseDTO actualizarProductoPorId(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El producto con el ID provisto no existe"));
        validarDatos(dto, false);

        // Editar un producto no debe renombrar un negocio o categoría compartidos.
        if (dto.getNegocioId() != null && !dto.getNegocioId().equals(producto.getNegocio().getId())) {
            throw new BadRequestException("No se puede cambiar el negocio de un producto");
        }
        if (dto.getNombreNegocio() != null && !dto.getNombreNegocio().isBlank()
                && !dto.getNombreNegocio().equals(producto.getNegocio().getNombre())) {
            throw new BadRequestException("El negocio se renombra desde su propio endpoint");
        }
        if (dto.getCategoriaId() != null) {
            producto.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new NotFoundException("La categoria solicitada no existe")));
        }
        if (dto.getNombreCategoria() != null && !dto.getNombreCategoria().isBlank()
                && (producto.getCategoria() == null
                    || !dto.getNombreCategoria().equals(producto.getCategoria().getNombre()))) {
            throw new BadRequestException("La categoria se renombra desde su propio endpoint");
        }
        if (dto.getNombre() != null) producto.setNombre(dto.getNombre().trim());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getPrecio() != null) producto.setPrecio(precioValido(dto.getPrecio()));
        if (dto.getStock() != null) producto.setStock(dto.getStock());

        return convertir(productoRepository.save(producto));
    }

    @Transactional
    public void borrarProductoPorId(Long id) {
        if (productoRepository.findById(id).isEmpty()) {
            throw new NotFoundException("El producto con el ID provisto no existe");
        }
        if (detalleVentaRepository.existsByProductoId(id)
                || valoracionProductoRepository.existsByProductoId_Id(id)) {
            throw new ConflictException("No se puede eliminar un producto con ventas o valoraciones asociadas");
        }
        productoRepository.deleteById(id);
    }

    private void validarDatos(ProductoRequestDTO dto, boolean crear) {
        if (dto == null) throw new BadRequestException("Datos del producto obligatorios");
        if (crear && (dto.getNegocioId() == null || dto.getCategoriaId() == null)) {
            throw new BadRequestException("Se requieren negocioId y categoriaId");
        }
        if ((crear && dto.getNombre() == null)
                || (dto.getNombre() != null && dto.getNombre().isBlank())) {
            throw new BadRequestException("Nombre invalido");
        }
        if (crear && (dto.getDescripcion() == null || dto.getDescripcion().isBlank())) {
            throw new BadRequestException("Descripcion invalida");
        }
        if (crear && dto.getPrecio() == null) throw new BadRequestException("Precio invalido");
        if (dto.getPrecio() != null) precioValido(dto.getPrecio());
        if (crear && dto.getStock() == null) throw new BadRequestException("Stock invalido");
        if (dto.getStock() != null && dto.getStock() < 0) throw new BadRequestException("Stock invalido");
    }

    private BigDecimal precioValido(Double precio) {
        if (precio == null || !Double.isFinite(precio) || precio < 0) {
            throw new BadRequestException("Precio invalido");
        }
        BigDecimal resultado = BigDecimal.valueOf(precio).setScale(2, RoundingMode.HALF_UP);
        if (resultado.precision() > 10) throw new BadRequestException("Precio fuera de rango");
        return resultado;
    }

    private ProductoResponseDTO convertir(Producto producto) {
        Categoria categoria = producto.getCategoria(); // Puede ser null (ON DELETE SET NULL)
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNegocio().getId(), producto.getNegocio().getNombre(),
                categoria == null ? null : categoria.getId(),
                categoria == null ? null : categoria.getNombre(),
                producto.getNombre(), producto.getDescripcion(),
                producto.getPrecio().doubleValue(), producto.getStock());
    }
}
