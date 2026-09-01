package com.desarrolloweb.NegocioApp.categoriaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.dtos.categoriaDTO.CategoriaResponseDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

	@Mock
	CategoriaRepository categoriaRepository; // Modulo simulado

	@Mock
	ProductoRepository productoRepository; // Modulo simulado

	@InjectMocks
	CategoriaService categoriaService; // Modulo principal

	// ##################################################
	
	@Test
	void obtenerTodasCategorias_Valido() {
	    Integer page = 1;
	    Integer limit = 20;
	    Categoria c1 = new Categoria(1L, "Hogar", "Productos para el hogar");
        Categoria c2 = new Categoria(2L, "Computacion", "Productos sobre computacion");
        List<Categoria> listaC = List.of(c1, c2);
	    Pageable paginaConf = PageRequest.of(0, limit);
        Page<Categoria> pagina = new PageImpl<>(listaC, paginaConf, 2);
        when(categoriaRepository.findAll(any(Pageable.class))).thenReturn(pagina);
        
	    PaginacionDTO<CategoriaResponseDTO> resultado = categoriaService.obtenerTodasCategorias(page, limit);
	    
        assertNotNull(resultado.getData());
        assertEquals(2, resultado.getData().size());
        assertEquals("Hogar", resultado.getData().get(0).getNombre());
        MetaDTO meta = resultado.getMeta();
        assertEquals(2, meta.getTotalItems());
        assertEquals(2, meta.getItemCount());
        assertEquals(20, meta.getItemsPerPage());
        assertEquals(1, meta.getTotalPages());
        assertEquals(1, meta.getCurrentPage());
        verify(categoriaRepository, times(1)).findAll(paginaConf);
    }
	
	// ##################################################
	
	@Test
	void obtenerCategoriaPorId_Exitoso() {
	    Long id = 1L;
	    Categoria respuesta = new Categoria(1L, "Hogar", "Productos para el hogar");
	    when(categoriaRepository.findById(id)).thenReturn(Optional.of(respuesta));
	    
	    CategoriaResponseDTO respuestaService = categoriaService.obtenerCategoriaPorId(id);
	    
	    assertEquals(respuesta.getId(), respuestaService.getId());
	    assertEquals(respuesta.getNombre(), respuestaService.getNombre());
	    assertEquals(respuesta.getDescripcion(), respuestaService.getDescripcion());
	    verify(categoriaRepository, times(1)).findById(id);
	}
	
	@Test
	void obtenerCategoriaPorId_Invalido() {
	    Long id = 1L;
	    when(categoriaRepository.findById(id)).thenReturn(Optional.empty());
	    
	    Exception excepcion = assertThrows(NotFoundException.class, () -> {
            categoriaService.obtenerCategoriaPorId(id); 
        });
        
        assertEquals("El elemento solicitado no existe", excepcion.getMessage());
        verify(categoriaRepository, times(1)).findById(id);
	}
	
	// ##################################################
	
	@Test
	void crearCategoria() {
	    
	}
	
	// ##################################################
	
	@Test
	void actualizarCategoria() {
	    
	}
	
	// ##################################################

	@Test
	void borrarCategoriaPorId_Inexistente() {
		Long id = 1L;
		when(categoriaRepository.existsById(id)).thenReturn(false);

		Exception excepcion = assertThrows(NotFoundException.class, () -> {
			categoriaService.borrarCategoriaPorId(id);
		});

        assertEquals("La categoria con el ID provisto no existe", excepcion.getMessage());
		verify(categoriaRepository, times(1)).existsById(anyLong());
		verify(productoRepository, never()).existsByCategoriaId(anyLong());
		verify(categoriaRepository, never()).deleteById(anyLong());
	}

	@Test
	void borrarCategoriaPorId_ProductosAsociados() {
		Long id = 1L;
		when(categoriaRepository.existsById(id)).thenReturn(true);
		when(productoRepository.existsByCategoriaId(id)).thenReturn(true);

		Exception excepcion = assertThrows(ConflictException.class, () -> {
			categoriaService.borrarCategoriaPorId(id);
		});
		
        assertEquals("La categoria provista no puede eliminarse debido a que tiene productos asociados", excepcion.getMessage());
		verify(categoriaRepository, times(1)).existsById(anyLong());
		verify(productoRepository, times(1)).existsByCategoriaId(anyLong());
		verify(categoriaRepository, never()).deleteById(anyLong());
	}

	@Test
	void borrarCategoriaPorId_Exitoso() {
		Long id = 1L;
		when(categoriaRepository.existsById(id)).thenReturn(true);
		when(productoRepository.existsByCategoriaId(id)).thenReturn(false);

		categoriaService.borrarCategoriaPorId(id);

		verify(categoriaRepository, times(1)).existsById(anyLong());
		verify(productoRepository, times(1)).existsByCategoriaId(anyLong());
		verify(categoriaRepository, times(1)).deleteById(anyLong());
	}
}
