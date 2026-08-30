package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;

import com.desarrolloweb.NegocioApp.repository.ProductoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // ##################################################

    // Obtener lista de productos (paginadas)
    public PaginacionDTO<ProductoDTO> obtenerTodosProductos(Integer page, Integer limit) { 
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Producto> paginaProductos = productoRepository.findAll(pageable);
        List<Producto> productos = paginaProductos.getContent();
        
        List<ProductoDTO> dtos = new ArrayList<>();
        for (Producto p : productos) {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(p.getId());
            dto.setNegocioId(p.getNegocio().getId());
            dto.setNombreNegocio(p.getNegocio().getNombre());
            dto.setCategoriaId(p.getNegocio().getId());
            dto.setNombreCategoria(p.getNegocio().getNombre());
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
                p.getNegocio().getId(),
                p.getNegocio().getNombre(),
                p.getNegocio().getId(),
                p.getNegocio().getNombre(),
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
    public ProductoDTO crearProducto(ProductoDTO pDTO) {

        Producto newP = new Producto(); // negocio -> buscar por id | categoria -> buscar por id

        // Verificar nombre negocio
        if (pDTO.getNombreNegocio() == null || pDTO.getNombreNegocio().isEmpty()) { throw new BadRequestException("Nombre del negocio invalido"); }
        // Verificar nombre categoria
        if (pDTO.getNombreCategoria() == null || pDTO.getNombreCategoria().isEmpty()) { throw new BadRequestException("Nombre de categoria invalido"); }
        // Verificar nombre
        if (pDTO.getNombre() == null || pDTO.getNombre().isEmpty()) { throw new BadRequestException("Nombre invalido"); }
        // Verificar descripcion
        if (pDTO.getDescripcion() == null || pDTO.getDescripcion().isEmpty()) { throw new BadRequestException("Descripcion invalida"); }
        // Verificar precio
        if (pDTO.getPrecio() == null) { throw new BadRequestException("Precio invalido"); }
        // Verificar stock
        if (pDTO.getStock() == null) { throw new BadRequestException("Stock invalido"); }
        
        Producto p = productoRepository.save(newP);
        
        return new ProductoDTO(
            p.getId(),
            p.getNegocio().getId(),
            p.getNegocio().getNombre(),
            p.getNegocio().getId(),
            p.getNegocio().getNombre(),
            p.getNombre(),
            p.getDescripcion(),
            p.getPrecio(),
            p.getStock()
        );
        
    }

    // ##################################################

    // Actualizar producto por id
    public ProductoDTO actualizarProductoPorId(Long id, ProductoDTO pDTO) {

        Optional<Producto> optP = productoRepository.findById(id);

        if (optP.isPresent()) {
            Producto newP = optP.get();
            
            if (pDTO.getNombreNegocio() != null || !pDTO.getNombreNegocio().isBlank()) {
                newP.getNegocio().setNombre(pDTO.getNombreNegocio());
            }            
            if (pDTO.getNombreCategoria() != null || !pDTO.getNombreCategoria().isBlank()) {
                newP.getCategoria().setNombre(pDTO.getNombreCategoria());
            }            
            if (pDTO.getNombre() != null || !pDTO.getNombre().isBlank()) {
                newP.setNombre(pDTO.getNombre());
            }
            if (pDTO.getDescripcion() != null || !pDTO.getDescripcion().isBlank()) {
                newP.setDescripcion(pDTO.getDescripcion());
            }
            if (pDTO.getPrecio() != null) {
                newP.setPrecio(pDTO.getPrecio());
            }            
            if (pDTO.getStock() != null) {
                newP.setStock(pDTO.getStock());
            }
            
            Producto p = productoRepository.save(newP);
            return new ProductoDTO(
                p.getId(),
                p.getNegocio().getId(),
                p.getNegocio().getNombre(),
                p.getNegocio().getId(),
                p.getNegocio().getNombre(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock()
            );
        }
        else {
            throw new NotFoundException("La categoria con el ID provisto no existe");
        }  
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
}
