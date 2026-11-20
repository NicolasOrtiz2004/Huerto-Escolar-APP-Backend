package cl.duoc.mshuerto.dto;

/**
 * Formato ÚNICO de error para todos los microservicios:
 *   { "mensaje": "Usuario o contraseña incorrectos" }
 * La app Android lee este campo y lo muestra tal cual al usuario.
 */
// Formato único de error: { "mensaje": "..." }
public record ErrorResponse(String mensaje) {
}
