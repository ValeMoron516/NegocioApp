package com.desarrolloweb.NegocioApp.productoTest;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.productoDTO.ProductoResponseDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    ProductoRepository productoRepository; // Módulo simulado

    @InjectMocks
    ProductoService productoService; // Módulo principal

    // Método de ayuda para crear un producto
    private Producto crearProductoMock(Long id, String nombre, Double precio) {
        Negocio n = new Negocio();
        n.setId(1L);
        n.setNombre("Negocio de Prueba");

        Categoria c = new Categoria();
        c.setId(1L);
        c.setNombre("Electrónica");

        Producto p = new Producto();
        p.setId(id);
        p.setNegocio(n);
        p.setCategoria(c);
        p.setNombre(nombre);
        p.setDescripcion("Descripción de " + nombre);
        p.setPrecio(new BigDecimal(precio));
        p.setStock(10);
        return p;
    }

    // ##################################################
    
    @Test
    void obtenerTodosProductos_Valido() {
        Integer page = 1;
        Integer limit = 20;
        
        Producto p1 = crearProductoMock(1L, "Mouse", 15000.0);
        Producto p2 = crearProductoMock(2L, "Teclado", 45000.0);
        List<Producto> listaP = List.of(p1, p2);
        
        Pageable paginaConf = PageRequest.of(0, limit);
        Page<Producto> pagina = new PageImpl<>(listaP, paginaConf, 2);
        
        when(productoRepository.findAll(any(Pageable.class))).thenReturn(pagina);
        
        PaginacionDTO<ProductoResponseDTO> resultado = productoService.obtenerTodosProductos(page, limit);
        
        assertNotNull(resultado.getData());
        assertEquals(2, resultado.getData().size());
        assertEquals("Mouse", resultado.getData().get(0).getNombre());
        
        MetaDTO meta = resultado.getMeta();
        assertEquals(2, meta.getTotalItems());
        assertEquals(2, meta.getItemCount());
        assertEquals(20, meta.getItemsPerPage());
        assertEquals(1, meta.getTotalPages());
        assertEquals(1, meta.getCurrentPage());
        
        verify(productoRepository, times(1)).findAll(paginaConf);
    }
    
    // ##################################################
    
    @Test
    void obtenerProductoPorId_Exitoso() {
        Long id = 1L;
        Producto pMock = crearProductoMock(id, "Monitor", 120000.0);
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(pMock));
        
        ProductoResponseDTO respuestaService = productoService.obtenerProductoPorId(id);
        
        assertEquals(pMock.getId(), respuestaService.getId());
        assertEquals(pMock.getNombre(), respuestaService.getNombre());
        assertEquals(pMock.getPrecio().doubleValue(), respuestaService.getPrecio());
        verify(productoRepository, times(1)).findById(id);
    }
    
    @Test
    void obtenerProductoPorId_Invalido() {
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());
        
        Exception excepcion = assertThrows(NotFoundException.class, () -> {
            productoService.obtenerProductoPorId(id); 
        });
        
        assertEquals("El elemento solicitado no existe", excepcion.getMessage());
        verify(productoRepository, times(1)).findById(id);
    }
    
    // ##################################################
    
    @Test
    void crearProducto_Valido() {
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio de Prueba", 1L, "Electrónica", "Webcam", "Cámara HD", 25000.0, 5
        );
        
        Producto pGuardado = crearProductoMock(1L, "Webcam", 25000.0);
        
        when(productoRepository.save(any(Producto.class))).thenReturn(pGuardado);
        
        ProductoResponseDTO respuestaService = productoService.crearProducto(peticion);
        
        assertEquals(1L, respuestaService.getId());
        assertEquals(peticion.getNombre(), respuestaService.getNombre());
        assertEquals(peticion.getPrecio(), respuestaService.getPrecio());
        
        verify(productoRepository, times(1)).save(any(Producto.class));
    }
    
    @Test
    void crearProducto_NombreInvalido() {
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio", 1L, "Cat", "", "Desc", 10.0, 5
        );
        
        Exception excepcion = assertThrows(BadRequestException.class, () -> {
            productoService.crearProducto(peticion);
        });
        
        assertEquals("Nombre invalido", excepcion.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void crearProducto_PrecioInvalido() {
        ProductoRequestDTO peticion = new ProductoRequestDTO(
            1L, "Negocio", 1L, "Cat", "Prod", "Desc", null, 5
        );
        
        Exception excepcion = assertThrows(BadRequestException.class, () -> {
            productoService.crearProducto(peticion);
        });
        
        assertEquals("Precio invalido", excepcion.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
    }
    
    // ##################################################
    
    @Test
    void actualizarProducto_Valido() {
        Long id = 1L;
        ProductoRequestDTO peticion = new ProductoRequestDTO();
        peticion.setNombre("Monitor 4K");
        peticion.setPrecio(150000.0);
        
        Producto pExistente = crearProductoMock(id, "Monitor HD", 120000.0);
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(pExistente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);
        
        ProductoResponseDTO resp = productoService.actualizarProductoPorId(id, peticion);
        
        assertEquals("Monitor 4K", resp.getNombre());
        assertEquals(150000.0, resp.getPrecio());
        
        assertEquals(10, resp.getStock()); 
        
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }
    
    @Test
    void actualizarProducto_IdNoExistente() {
        Long id = 1L;
        ProductoRequestDTO peticion = new ProductoRequestDTO();
        
        when(productoRepository.findById(id)).thenReturn(Optional.empty());
        
        Exception ex = assertThrows(NotFoundException.class,  () -> {
            productoService.actualizarProductoPorId(id, peticion);
        }); 
        
        assertEquals("El producto con el ID provisto no existe", ex.getMessage());
        
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, never()).save(any(Producto.class));
    }
    
    // ##################################################

    @Test
    void borrarProductoPorId_Exitoso() {
        Long id = 1L;
        Producto pExistente = crearProductoMock(id, "Borrador", 100.0);
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(pExistente));

        productoService.borrarProductoPorId(id);

        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).deleteById(id);
    }
    
    @Test
    void borrarProductoPorId_Inexistente() {
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        Exception excepcion = assertThrows(NotFoundException.class, () -> {
            productoService.borrarProductoPorId(id);
        });

        assertEquals("El producto con el ID provisto no existe", excepcion.getMessage());
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, never()).deleteById(anyLong());
    }
}
