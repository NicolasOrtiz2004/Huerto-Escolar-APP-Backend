package cl.duoc.msusuarios.dto;

import cl.duoc.msusuarios.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear una cuenta nueva (POST /api/usuarios).
 * Las anotaciones validan el JSON ANTES de llegar al servicio: si algo
 * falla, el ManejadorErrores responde 400 con el mensaje.
 */
// record = clase inmutable: Java crea solo el constructor, los getters (nombre(), rol()...), equals y toString
public record UsuarioRequest(
        // Usuario para iniciar sesión: 3 a 30 caracteres y sin espacios (\\S+ = "sin espacios")
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
        @Pattern(regexp = "^\\S+$", message = "El usuario no puede contener espacios")
        String nombreUsuario,

        // Nombre para mostrar
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 60, message = "El nombre debe tener entre 3 y 60 caracteres")
        String nombre,

        // Contraseña en texto: el servicio la convierte en hash antes de guardar
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres")
        String password,

        // Debe ser "ADMINISTRADOR" o "ESTUDIANTE"; otro valor → 400 (JSON inválido)
        @NotNull(message = "El rol es obligatorio (ADMINISTRADOR o ESTUDIANTE)")
        Rol rol,

        // Taller o curso
        @NotBlank(message = "El taller es obligatorio")
        @Size(max = 60, message = "El taller no puede superar 60 caracteres")
        String taller
) {
}
