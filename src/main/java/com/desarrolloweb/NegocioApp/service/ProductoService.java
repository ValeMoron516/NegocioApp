package com.desarrolloweb.NegocioApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // ##################################################

    // Obtener lista de productos (paginadas)
    public PaginacionDTO<ProductoDTO> obtenerTodosProductos(Integer page, Integer limit) { 
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Producto> paginaProductos = productoRepository.findAll(pageable);
        List<Producto> productos = paginaProductoss.getContent();
        
        List<ProductoDTO> dtos = new ArrayList<>();
        for (Producto p : productos) {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(p.getId());
            dto.setNegocioId(p.getNegocioId());
            dto.setNombreNegocio(p.getNombreNegocio());
            dto.setCategoriaId(p.getCategoriaId());
            dto.setNombreCategoria(p.getNombreCategoria());
            dto.setNombre(p.getNombre());
            dto.setDescripcion(p.getDescripcion());
            dto.setPrecio(p.getPrecio());
            dto.setStock(p.getStock());
            
            dtos.add(dto);
        }
        
        MetaDTO meta = new MetaDTO(
            paginaProductos.getTotalElements(),     // totalItems   (elementos totales)
            paginaProductos.getNumberOfElements(),  // itemCount    (elementos en la página actual)
            paginaProductos.getSize(),              // itemsPerPage (elementos maximos por pagina)
            paginaProductos.getTotalPages(),        // totalPages   (total de paginas)
            paginaProductos.getNumber() + 1         // currentPage  (pagina actual)
        );
    
        return new PaginacionDTO<>(dtos, meta);
    }

    // ##################################################

    // Obtener producto por ID
    public ProductoDTO obtenerProductoPorId(Long id) { 
        Optional<Producto> optP = productoRepository.findById(id);
        
        // Existe
        if (optP.isPresent()) {
            Producto p = optP.get();
            
            return new ProductoDTO(
            p.getId(),
            p.getNegocioId(),
            p.getNombreNegocio(),
            p.getCategoriaId(),
            p.getNombreCategoria(),
            p.getNombre(),
            p.getDescripcion(),
            p.getPrecio(),
            p.getStock()
            );
        }
        
        // No existe
        throw new NotFoundException("El elemento solicitado no existe");
    }

    // ##################################################

    // Crear nuevo producto
    public ProductoDTO crearProducto(ProductoDTO p) {

        // Verificar nombre negocio
        if (p.nombreNegocio == null || p.nombreNegocio.isEmpty()) { throw new BadRequestException("Nombre del negocio invalido"); }
        // Verificar nombre categoria
        if (p.nombreCategoria == null || p.nombreCategoria.isEmpty()) { throw new BadRequestException("Nombre de categoria invalido"); }
        // Verificar nombre
        if (p.nombre == null || p.nombre.isEmpty()) { throw new BadRequestException("Nombre invalido"); }
        // Verificar descripcion
        if (p.descripcion == null || p.descripcion.isEmpty()) { throw new BadRequestException("Descripcion invalida"); }
        // Verificar precio
        if (p.precio == null || p.precio.isEmpty()) { throw new BadRequestException("Precio invalido"); }
        // Verificar stock
        if (p.stock == null || p.stock.isEmpty()) { throw new BadRequestException("Stock invalido"); }
        
        Producto p = productoRepository.save(p);
        
        return new ProductoDTO(
            p.getId(),
            p.getNegocioId(),
            p.getNombreNegocio(),
            p.getCategoriaId(),
            p.getNombreCategoria(),
            p.getNombre(),
            p.getDescripcion(),
            p.getPrecio(),
            p.getStock()
        );
        
    }

    // ##################################################

    // Actualizar producto por id
    public ProductoDTO actualizarProductoPorId(Long id, Producto p) {
        Optional<Producto> optP = productoRepository.findById(id);

        if (optP.isPresent()) {
            Producto newP = optP.get();
            
            if (p.getNombreNegocio() != null || !p.getNombreNegocio().isBlank()) {
                newP.setNombreNegocio(p.getNombreNegocio());
            }            
            if (p.getNombreCategoria() != null || !p.getNombreCategoria().isBlank()) {
                newP.setNombreCategoria(p.getNombreCategoria());
            }            
            if (p.getNombre() != null || !p.getNombre().isBlank()) {
                newP.setNombre(p.getNombre());
            }
            if (p.getDescripcion() != null || !p.getDescripcion().isBlank()) {
                newP.setDescripcion(p.getDescripcion());
            }
            if (p.getPrecio() != null || !p.getPrecio().isBlank()) {
                newP.setPrecio(p.getPrecio());
            }            
            if (p.getStock() != null || !p.getStock().isBlank()) {
                newP.setStock(p.getStock());
            }
            
            Producto p = productoRepository.save(newP);
            return new ProductoDTO(
                p.getId(),
                p.getNegocioId(),
                p.getNombreNegocio(),
                p.getCategoriaId(),
                p.getNombreCategoria(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock()
            );
        }
        else {
            throw new NotFoundException("La categoria con el ID provisto no existe");  
   }

   // ##################################################

    // Borrar producto por id
    public void borrarProductoPorId(Long id) {
         
         Optional<Producto> optP = productoRepository.findById(id);

        if (optP.isPresent()) {
            productoRepository.deleteById(id);
        } else {
            throw new NotFoundException("La categoria con el ID provisto no existe");
    }
}
