package cl.duoc.mshuerto.dto;

/**
 * Copia del JSON que responde ms-usuarios en GET /api/usuarios/{id}.
 * Solo se usa para LEER la respuesta de otro microservicio.
 */
public record UsuarioRemoto(
        // Id del usuario en ms-usuarios
        Long id,
        // Nombre de usuario
        String nombreUsuario,
        // Nombre para mostrar
        String nombre,
        // "ADMINISTRADOR" o "ESTUDIANTE" (como texto, porque el enum Rol vive en el otro microservicio)
        String rol,
        // Taller
        String taller,
        // Si la cuenta está activa
        boolean activo
) {
}
