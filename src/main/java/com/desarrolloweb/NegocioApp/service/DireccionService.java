package com.desarrolloweb.NegocioApp.service;
import org.springframework.stereotype.Service;
import java.util.List;

import com.desarrolloweb.NegocioApp.dto.DireccionResponseDTO;
import com.desarrolloweb.NegocioApp.dto.DireccionRequestDTO;
import com.desarrolloweb.NegocioApp.repository.DireccionRepository;
import com.desarrolloweb.NegocioApp.entity.Direccion; 


@Service
public class DireccionService {
    private final DireccionRepository direccionRepository;


    public DireccionService(DireccionRepository direccionRepository) {
    this.direccionRepository = direccionRepository;
    }

    public DireccionResponseDTO crearDireccion (DireccionRequestDTO dto){
        Direccion direccion = new Direccion();
        direccion.setCalle(dto.getCalle());
        direccion.setUsuarioId(dto.getUsuarioId());
        direccion.setNumero(dto.getNumero());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCodigoPostal(dto.getCodigoPostal());

        Direccion direccionGuardada = direccionRepository.save(direccion);

        return new DireccionResponseDTO(
           direccionGuardada.getId(),
           direccionGuardada.getUsuarioId(),
           direccionGuardada.getCalle(),
           direccionGuardada.getNumero(),
           direccionGuardada.getCodigoPostal(),
           direccionGuardada.getCiudad()
      );

    }

     public DireccionResponseDTO obtenerDireccionPorId (Long id){

        Direccion direccion = direccionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dirección no encontrada con el ID: " + id));

      return new DireccionResponseDTO(
        direccion.getId(),
        direccion.getUsuarioId(),
        direccion.getCalle(),
        direccion.getNumero(),
        direccion.getCodigoPostal(),
        direccion.getCiudad()
        );
       }

    public List <DireccionResponseDTO> obtenerDireccionPorUsuario (Long usuarioId){

        List <Direccion> direcciones = direccionRepository.findByUsuarioId(usuarioId);

        return direcciones.stream()
        .map(dir -> new DireccionResponseDTO(
            dir.getId(),
            dir.getUsuarioId(),
            dir.getCalle(),
            dir.getNumero(),
            dir.getCodigoPostal(),
            dir.getCiudad()
        ))
        .toList();

    }

    public DireccionResponseDTO actualizarDireccion (Long id, DireccionRequestDTO dto){

        Direccion direccion = direccionRepository.findById(id)
        .orElseThrow (()-> new RuntimeException("Direccion no encontrada con el id: " + id));

        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setCiudad(dto.getCiudad());
        direccion.setUsuarioId(dto.getUsuarioId());

        Direccion direccionActualizada = direccionRepository.save(direccion);

        return new DireccionResponseDTO(
            direccionActualizada.getId(),
            direccionActualizada.getUsuarioId(),
            direccionActualizada.getCalle(),
            direccionActualizada.getNumero(),
            direccionActualizada.getCodigoPostal(),
            direccionActualizada.getCiudad()
        );


    }

    public void eliminarDireccion (Long id){

        if(!direccionRepository.existsById(id)){
            throw new RuntimeException("No se puede eliminar, direccion con el id " + id + " no encontrada.");
        }
        direccionRepository.deleteById(id);
    }



 }



