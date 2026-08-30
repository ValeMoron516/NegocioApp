package com.desarrolloweb.NegocioApp.categoriaTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    // ##################################################
    // ##################################################
    // ##################################################

    @Test
    void borrarCategoriaPorId_Inexistente() {
        Long id = 1L;
        when(categoriaRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            categoriaService.borrarCategoriaPorId(id);
        });

        verify(categoriaRepository, times(1)).existsById(anyLong());
        verify(productoRepository, never()).existsByCategoriaId(anyLong());
        verify(categoriaRepository, never()).deleteById(anyLong());
    }

    @Test
    void borrarCategoriaPorId_ProductosAsociados() {
        Long id = 1L;
        when(categoriaRepository.existsById(id)).thenReturn(true);
        when(productoRepository.existsByCategoriaId(id)).thenReturn(true);

        assertThrows(ConflictException.class, () -> {
            categoriaService.borrarCategoriaPorId(id);
        });

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
