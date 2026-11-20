package cl.duoc.mshuerto.client;

import cl.duoc.mshuerto.dto.UsuarioRemoto;
import cl.duoc.mshuerto.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

/*
 * CLIENTE HTTP HACIA ms-usuarios (comunicación ENTRE microservicios)
 * ==================================================================
 * Como cada microservicio tiene su propia base de datos, ms-huerto NO puede
 * hacer un JOIN con la tabla usuarios. En vez de eso le PREGUNTA a
 * ms-usuarios por HTTP:  GET http://localhost:8081/api/usuarios/{id}
 *
 * RestClient es el cliente HTTP moderno de Spring (reemplaza a RestTemplate).
 * La URL viene de application.properties (servicios.usuarios.url).
 */
// @Component: Spring crea este objeto y lo inyecta en SectorService
@Component
public class UsuarioClient {

    // Cliente HTTP ya configurado con la URL base de ms-usuarios
    private final RestClient restClient;

    // @Value lee la propiedad servicios.usuarios.url de application.properties
    public UsuarioClient(@Value("${servicios.usuarios.url}") String urlUsuarios) {
        this.restClient = RestClient.builder().baseUrl(urlUsuarios).build();
    }

    /**
     * Regla del caso: el responsable de un sector debe ser un docente o
     * tallerista (ADMINISTRADOR) con la cuenta activa.
     * Si no cumple, lanza ApiException con el motivo.
     */
    public void verificarResponsable(Long usuarioId) {
        UsuarioRemoto usuario;
        try {
            // GET http://localhost:8081/api/usuarios/{id} → el JSON se convierte en UsuarioRemoto
            usuario = restClient.get()
                    .uri("/api/usuarios/{id}", usuarioId)
                    .retrieve()
                    .body(UsuarioRemoto.class);
        // ms-usuarios respondió 404 → ese usuario no existe
        } catch (HttpClientErrorException.NotFound e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El responsable seleccionado no existe");
        } catch (ResourceAccessException e) {
            // ms-usuarios está apagado o no responde
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo verificar el responsable: ms-usuarios no responde");
        }

        // Regla: solo un docente o tallerista puede ser responsable
        if (usuario == null || !"ADMINISTRADOR".equals(usuario.rol())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El responsable debe ser un docente o tallerista");
        }
        // Regla: la cuenta del responsable debe estar activa
        if (!usuario.activo()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La cuenta del responsable está desactivada");
        }
    }
}
