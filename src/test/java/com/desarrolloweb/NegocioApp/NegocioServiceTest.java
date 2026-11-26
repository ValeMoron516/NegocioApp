package com.desarrolloweb.NegocioApp;

import com.desarrolloweb.NegocioApp.dto.negocio.NegocioRequestDTO;
import com.desarrolloweb.NegocioApp.dto.negocio.NegocioResponseDTO;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.service.NegocioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NegocioServiceTest {

    @Mock NegocioRepository negocioRepository;
    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks NegocioService negocioService;

    @Test
    void actualizarGuardaYDevuelveDatosDeTransferenciaYContacto() {
        Usuario propietario = new Usuario();
        propietario.setId(7L);
        Negocio negocio = new Negocio();
        negocio.setId(3L);
        negocio.setUsuario(propietario);
        negocio.setEstadoAprobacion("APROBADO");

        NegocioRequestDTO request = new NegocioRequestDTO();
        request.setNombre("Comercio actualizado");
        request.setDescripcion("Descripcion actualizada");
        request.setImagenUrl(" https://ejemplo.com/comercio.jpg ");
        request.setAliasBancario(" comercio.pagos ");
        request.setTitularCuenta(" Ana Perez ");
        request.setWhatsappComercial(" +5491112345678 ");

        when(negocioRepository.findById(3L)).thenReturn(Optional.of(negocio));
        when(negocioRepository.save(negocio)).thenReturn(negocio);

        NegocioResponseDTO respuesta = negocioService.actualizar(3L, request);

        assertEquals("https://ejemplo.com/comercio.jpg", respuesta.getImagenUrl());
        assertEquals("comercio.pagos", respuesta.getAliasBancario());
        assertEquals("Ana Perez", respuesta.getTitularCuenta());
        assertEquals("+5491112345678", respuesta.getWhatsappComercial());
        verify(negocioRepository).save(negocio);
    }

    @Test
    void consultaPublicaNoExponeDatosDeTransferencia() {
        Usuario propietario = new Usuario();
        propietario.setId(7L);
        Negocio negocio = new Negocio();
        negocio.setId(3L);
        negocio.setUsuario(propietario);
        negocio.setEstadoAprobacion("APROBADO");
        negocio.setAliasBancario("comercio.pagos");
        negocio.setTitularCuenta("Ana Perez");
        negocio.setWhatsappComercial("+5491112345678");
        when(negocioRepository.findById(3L)).thenReturn(Optional.of(negocio));

        NegocioResponseDTO respuesta = negocioService.buscarPorId(3L);

        assertNull(respuesta.getAliasBancario());
        assertNull(respuesta.getTitularCuenta());
        assertNull(respuesta.getWhatsappComercial());
    }
}