package cl.duoc.mssensores.dto;

import cl.duoc.mssensores.model.EstadoLectura;
import cl.duoc.mssensores.model.LecturaSensor;

import java.time.LocalDateTime;

/** JSON de una lectura. La app la guarda en su tabla lecturas_sensor de Room (caché). */
// JSON de respuesta de una lectura
public record LecturaResponse(
        // Id de la lectura
        Long id,
        // Sector al que pertenece
        Long sectorId,
        // Fecha y hora ("2026-11-20T09:15:00")
        LocalDateTime fecha,
        // Humedad del suelo en %
        double humedadSuelo,
        // Temperatura en °C
        double temperatura,
        // Humedad del aire en %
        double humedadAmbiente,
        // "Simulación académica" o "Ingreso manual de prueba"
        String origen,
        // Clasificación
        EstadoLectura estado
) {
    // Convierte la entidad en DTO
    public static LecturaResponse desde(LecturaSensor l) {
        return new LecturaResponse(l.getId(), l.getSectorId(), l.getFecha(), l.getHumedadSuelo(),
                l.getTemperatura(), l.getHumedadAmbiente(), l.getOrigen(), l.getEstado());
    }
}
