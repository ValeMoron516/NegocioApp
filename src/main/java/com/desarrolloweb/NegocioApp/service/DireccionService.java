package com.desarrolloweb.NegocioApp.service;
import org.springframework.stereotype.Service;
import java.util.List;

import com.desarrolloweb.NegocioApp.dto.direccion.DireccionResponseDTO;
import com.desarrolloweb.NegocioApp.dto.direccion.DireccionRequestDTO;
import com.desarrolloweb.NegocioApp.repository.DireccionRepository;
import com.desarrolloweb.NegocioApp.entity.Direccion;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException; 


@Service
public class DireccionService {
    private final DireccionRepository direccionRepository;


    public DireccionService(DireccionRepository direccionRepository) {
    this.direccionRepository = direccionRepository;
    }

    public DireccionResponseDTO crearDireccion (DireccionRequestDTO dto){
        validar(dto);
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

    // Obtiene todas las direcciones como DTO, sin exponer las entidades JPA.
    public List<DireccionResponseDTO> listarDirecciones() {
        return direccionRepository.findAll().stream()
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

     public DireccionResponseDTO obtenerDireccionPorId (Long id){

        Direccion direccion = direccionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Dirección no encontrada con el ID: " + id));

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
        validar(dto);
        Direccion direccion = direccionRepository.findById(id)
        .orElseThrow (()-> new NotFoundException("Direccion no encontrada con el id: " + id));

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
            throw new NotFoundException("No se puede eliminar, direccion con el id " + id + " no encontrada.");
        }
        direccionRepository.deleteById(id);
    }



     private void validar(DireccionRequestDTO dto) {
        if (dto == null || dto.getUsuarioId() == null)
            throw new BadRequestException("Direccion y usuario son obligatorios");
        if (dto.getCalle() == null || dto.getCalle().isBlank() || dto.getCalle().length() > 150
                || dto.getNumero() == null || dto.getNumero().isBlank() || dto.getNumero().length() > 50
                || dto.getCiudad() == null || dto.getCiudad().isBlank() || dto.getCiudad().length() > 100
                || dto.getCodigoPostal() == null || dto.getCodigoPostal().isBlank() || dto.getCodigoPostal().length() > 50) {
            throw new BadRequestException("Calle, numero, ciudad y codigo postal son obligatorios");
        }
    }
}



