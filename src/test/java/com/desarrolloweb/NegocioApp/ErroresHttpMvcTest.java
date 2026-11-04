package com.desarrolloweb.NegocioApp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.desarrolloweb.NegocioApp.controller.ApiExceptionHandler;
import com.desarrolloweb.NegocioApp.controller.AuthController;
import com.desarrolloweb.NegocioApp.dto.usuario.UsuarioRequestDTO;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.service.AuthService;
import com.desarrolloweb.NegocioApp.service.AccessGuard;

class ErroresHttpMvcTest {

    private MockMvc mvc(AuthService auth) {
        return MockMvcBuilders.standaloneSetup(new AuthController(auth, mock(AccessGuard.class)))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void loginConJsonNuloDevuelve400() throws Exception {
        mvc(mock(AuthService.class)).perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginConJsonInvalidoDevuelve400() throws Exception {
        mvc(mock(AuthService.class)).perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{esto no es json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registroConDatosInvalidosDevuelve400SinTraza() throws Exception {
        AuthService auth = mock(AuthService.class);
        when(auth.registrar(any(UsuarioRequestDTO.class)))
                .thenThrow(new BadRequestException("secreto-interno-de-prueba"));
        var result = mvc(auth).perform(post("/api/v1/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("secreto-interno-de-prueba"));
    }
}
