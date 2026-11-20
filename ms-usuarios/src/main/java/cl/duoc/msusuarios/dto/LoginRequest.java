package cl.duoc.msusuarios.dto;

import jakarta.validation.constraints.NotBlank;

/*
 * DTO (Data Transfer Object) = el "formato" del JSON que llega o sale.
 * Usamos record de Java: una clase inmutable con constructor y getters automáticos.
 *
 * JSON que envía la app:
 *   { "nombreUsuario": "docente", "password": "huerto123" }
 */
public record LoginRequest(
        // Nombre de usuario escrito en la app
        @NotBlank(message = "Ingresa tu nombre de usuario")
        String nombreUsuario,

        // Contraseña en texto (viaja solo en esta petición; nunca se guarda así)
        @NotBlank(message = "Ingresa tu contraseña")
        String password
) {
}
