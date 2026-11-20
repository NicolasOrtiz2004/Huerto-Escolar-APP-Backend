package cl.duoc.mshuerto.model;

/** Mismos valores que el enum EstadoSector de la app Android. */
// enum = lista CERRADA de valores. JPA lo guarda como texto (EnumType.STRING)
public enum EstadoSector {
    // Libre para usar
    DISPONIBLE,
    // Tiene cultivos activos
    EN_USO,
    // En reparación
    EN_MANTENIMIENTO,
    // No se pueden registrar cultivos aquí
    FUERA_DE_USO
}
