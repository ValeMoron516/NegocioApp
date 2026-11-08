package com.desarrolloweb.NegocioApp.controller;

import com.desarrolloweb.NegocioApp.service.SolicitudNegocioService;
import com.desarrolloweb.NegocioApp.service.SolicitudNegocioService.NuevaSolicitud;
import com.desarrolloweb.NegocioApp.service.SolicitudNegocioService.Rechazo;
import com.desarrolloweb.NegocioApp.service.SolicitudNegocioService.SolicitudVista;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/solicitudes-negocio")
public class SolicitudNegocioController {
    private final SolicitudNegocioService servicio;
    public SolicitudNegocioController(SolicitudNegocioService servicio){this.servicio=servicio;}
    @PostMapping
    public ResponseEntity<SolicitudVista> solicitar(@RequestBody NuevaSolicitud solicitud){
        return ResponseEntity.status(201).body(servicio.solicitar(solicitud));
    }
    @GetMapping("/mias")
    public List<SolicitudVista> mias(){return servicio.mias();}
    @GetMapping
    public List<SolicitudVista> listar(@RequestParam(required=false) String estado){return servicio.listar(estado);}
    @PostMapping("/{id}/aprobar")
    public SolicitudVista aprobar(@PathVariable Long id){return servicio.aprobar(id);}
    @PostMapping("/{id}/rechazar")
    public SolicitudVista rechazar(@PathVariable Long id,@RequestBody Rechazo rechazo){return servicio.rechazar(id,rechazo);}
}
