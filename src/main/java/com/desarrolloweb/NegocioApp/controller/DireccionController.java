package com.desarrolloweb.NegocioApp.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desarrolloweb.NegocioApp.dto.direccion.DireccionResponseDTO;
import com.desarrolloweb.NegocioApp.service.DireccionService;
import org.springframework.web.bind.annotation.RequestBody;
import com.desarrolloweb.NegocioApp.dto.direccion.DireccionRequestDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/direcciones")


public class DireccionController {
    @org.springframework.beans.factory.annotation.Autowired
    private com.desarrolloweb.NegocioApp.service.AccessGuard guard;

    private final DireccionService direccionService;

    public DireccionController (DireccionService direccionService){
        this.direccionService = direccionService;
    }

    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public DireccionResponseDTO crearDireccion (@RequestBody DireccionRequestDTO dto){
        guard.createsAddress(dto);


        return direccionService.crearDireccion(dto);
    }

    @DeleteMapping("/{id}")

    public void eliminarDireccion(@PathVariable Long id){
        guard.readsAddress(id);

        direccionService.eliminarDireccion(id);
    }

    // GET /api/v1/direcciones - Listar todas las direcciones
    // GET /api/v1/direcciones?usuarioId=3 - Filtrar por usuario
    @GetMapping
    public List<DireccionResponseDTO> listarDirecciones(
            @RequestParam(required = false) Long usuarioId) {
        if(usuarioId == null)guard.adminOnly();else guard.selfOrAdmin(usuarioId);

        return (usuarioId == null)
                ? direccionService.listarDirecciones()
                : direccionService.obtenerDireccionPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")

    public DireccionResponseDTO obtenerDireccionPorId (@PathVariable Long id){
        guard.readsAddress(id);


        return direccionService.obtenerDireccionPorId(id);
    }

     @PutMapping("/{id}")

    public DireccionResponseDTO actualizarDireccion (@PathVariable Long id, @RequestBody DireccionRequestDTO dto){
        guard.updatesAddress(id,dto);


        return direccionService.actualizarDireccion(id, dto);

    }




}
