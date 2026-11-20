package cl.duoc.msusuarios.dto;

/**
 * Formato ÚNICO de error para todos los microservicios:
 *   { "mensaje": "Usuario o contraseña incorrectos" }
 * La app Android lee este campo y lo muestra tal cual al usuario.
 */
// Un solo campo: el texto del error que se le muestra al usuario
public record ErrorResponse(String mensaje) {
}
