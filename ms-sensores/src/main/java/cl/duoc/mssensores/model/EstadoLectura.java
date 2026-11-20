package cl.duoc.mssensores.model;

/** Mismos valores que el enum EstadoLectura de la app Android. */
// Clasificación de una lectura según la humedad del suelo
public enum EstadoLectura {
    // Entre 30 % y 80 %
    NORMAL,
    // Menos de 30 % (suelo seco)
    BAJO,
    // Más de 80 % (suelo muy húmedo)
    ALTO,
    // Reservado para lecturas dudosas (igual que en la app)
    PENDIENTE_REVISION
}
