package cl.duoc.mshuerto.model;

/** Mismos valores que el enum EstadoCultivo de la app Android. */
// Estados de desarrollo de un cultivo (iguales a la app)
public enum EstadoCultivo {
    // Todavía no se siembra
    PLANIFICADO,
    // Recién sembrado
    SEMBRADO,
    // Creciendo
    EN_CRECIMIENTO,
    // Listo para cosechar
    LISTO_PARA_COSECHAR,
    // Ya se cosechó
    COSECHADO,
    // Tiene plagas, daño, etc.
    CON_PROBLEMAS,
    // Terminó su ciclo (se usa en vez de borrarlo si tiene historial)
    FINALIZADO
}
