package cl.duoc.msusuarios.dto;

import cl.duoc.msusuarios.model.Rol;
import cl.duoc.msusuarios.model.Usuario;

/**
 * Lo que el microservicio DEVUELVE de un usuario.
 * Importante: NO incluye el passwordHash (nunca se envía por la red).
 */
public record UsuarioResponse(
        // Id del usuario (la app lo usa como id en Room)
        Long id,
        // Usuario para iniciar sesión
        String nombreUsuario,
        // Nombre para mostrar
        String nombre,
        // ADMINISTRADOR o ESTUDIANTE (Jackson lo envía como texto)
        Rol rol,
        // Taller o curso
        String taller,
        // Si la cuenta está habilitada
        boolean activo
) {
    /** Convierte la entidad (tabla) en el DTO (JSON). */
    public static UsuarioResponse desde(Usuario u) {
        // Copia campo por campo desde la entidad. El passwordHash se deja AFUERA a propósito.
        return new UsuarioResponse(u.getId(), u.getNombreUsuario(), u.getNombre(), u.getRol(), u.getTaller(), u.isActivo());
    }
}
