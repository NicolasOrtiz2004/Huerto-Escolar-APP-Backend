package cl.duoc.msusuarios.model;

/**
 * Perfiles del caso. Los nombres son IGUALES a los del enum Rol de la app
 * Android, así el JSON viaja sin traducciones ("ADMINISTRADOR" / "ESTUDIANTE").
 */
public enum Rol {
    ADMINISTRADOR, // docente o tallerista
    ESTUDIANTE     // colaborador
}
