package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.dtos.CategoriaDTO;
import com.desarrolloweb.NegocioApp.dtos.PaginacionDTO;
import com.desarrolloweb.NegocioApp.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Leer todas las Categorias (paginadas)
    @GetMapping
    public ResponseEntity<PaginacionDTO<CategoriaDTO>> obtenerTodasCategorias(
        @RequestParam(defaultValue = "1") Integer page, 
        @RequestParam(defaultValue = "20") Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.obtenerTodasCategorias(page, limit));
    }

    // Leer Categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        Optional<CategoriaDTO> optDto = categoriaService.obtenerCategoriaPorId(id);
        
        // Existe
        if (optDto.isPresent()) {
            CategoriaDTO dto = optDto.get();
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }
        
        // No existe
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Crear
    //@PostMapping
    //public void crearCategoria(@RequestBody Categoria categoria) {
    //    categoriaService.crearCategoria(categoria);
    //}

    // Actualizar por id
    //@PutMapping("{id}")
    //public void actualizarCategoriaPorId(@PathVariable Long id, @RequestBody Categoria categoria) {
    //    categoriaService.actualizarCategoriaPorId(id, categoria);
    //}
    
    // Borrar por ID
    //@DeleteMapping("{id}")
    //public void borrarCategoriaPorId(@PathVariable Long id) {
    //    categoriaService.borrarCategoriaPorId(id);
    //}
}
