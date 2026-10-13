package com.desarrolloweb.NegocioApp.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrolloweb.NegocioApp.dto.DireccionResponseDTO;
import com.desarrolloweb.NegocioApp.service.DireccionService;
import org.springframework.web.bind.annotation.RequestBody;
import com.desarrolloweb.NegocioApp.dto.DireccionRequestDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/direcciones")


public class DireccionController {
    private final DireccionService direccionService;

    public DireccionController (DireccionService direccionService){
        this.direccionService = direccionService;
    }

    @PostMapping

    public DireccionResponseDTO crearDireccion (@RequestBody DireccionRequestDTO dto){

        return direccionService.crearDireccion(dto);
    }

    @DeleteMapping("/{id}")

    public void eliminarDireccion(@PathVariable Long id){
        direccionService.eliminarDireccion(id);
    }

    @GetMapping("/{id}")

    public DireccionResponseDTO obtenerDireccionPorId (@PathVariable Long id){

        return direccionService.obtenerDireccionPorId(id);
    }

     @PutMapping("/{id}")

    public DireccionResponseDTO actualizarDireccion (@PathVariable Long id, @RequestBody DireccionRequestDTO dto){

        return direccionService.actualizarDireccion(id, dto);

    }




}
