package cl.duoc.msusuarios.controller;

import cl.duoc.msusuarios.dto.LoginRequest;
import cl.duoc.msusuarios.dto.UsuarioResponse;
import cl.duoc.msusuarios.exception.ApiException;
import cl.duoc.msusuarios.model.Rol;
import cl.duoc.msusuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * PRUEBAS DEL CONTROLADOR (capa HTTP)
 * @WebMvcTest levanta SOLO la parte web (controlador + ManejadorErrores),
 * sin base de datos. MockMvc simula peticiones HTTP como si fuera Postman.
 * El servicio se reemplaza por un mock (@MockitoBean).
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    // MockMvc = "Postman de mentira": hace peticiones HTTP al controlador sin levantar el servidor
    @Autowired
    private MockMvc mockMvc;

    // @MockitoBean reemplaza el servicio real por un mock dentro de Spring
    @MockitoBean
    private UsuarioService usuarioService;

    // Prueba: POST /api/auth/login correcto → 200 con el usuario en JSON (y sin hash)
    @Test
    void loginCorrecto_responde200ConElUsuario() throws Exception {
        when(usuarioService.login(any(LoginRequest.class)))
                .thenReturn(new UsuarioResponse(1L, "docente", "Prof. Marta Quiroga", Rol.ADMINISTRADOR, "Taller de Huerto", true));

        // perform(post(...)) envía la petición; andExpect(...) revisa la respuesta
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUsuario\":\"docente\",\"password\":\"huerto123\"}"))
                .andExpect(status().isOk())
                // jsonPath("$.nombre") lee el campo "nombre" del JSON respondido
                .andExpect(jsonPath("$.nombre").value("Prof. Marta Quiroga"))
                .andExpect(jsonPath("$.rol").value("ADMINISTRADOR"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist()); // nunca viaja el hash
    }

    // Prueba: password vacía → @NotBlank falla → 400 con el mensaje de la validación
    @Test
    void loginSinPassword_responde400ConMensaje() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUsuario\":\"docente\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Ingresa tu contraseña"));
    }

    // Prueba: si el servicio lanza ApiException 401, el ManejadorErrores responde 401 con el mensaje
    @Test
    void credencialesIncorrectas_responde401() throws Exception {
        when(usuarioService.login(any(LoginRequest.class)))
                .thenThrow(new ApiException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombreUsuario\":\"docente\",\"password\":\"malaClave\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Usuario o contraseña incorrectos"));
    }
}
