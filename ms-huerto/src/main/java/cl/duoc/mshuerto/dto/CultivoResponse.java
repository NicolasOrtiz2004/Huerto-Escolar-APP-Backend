package cl.duoc.mshuerto.dto;

import cl.duoc.mshuerto.model.Cultivo;
import cl.duoc.mshuerto.model.EstadoCultivo;

import java.time.LocalDate;

/** Lo que devuelve el microservicio de un cultivo. Se envía sectorId (no el sector completo). */
// JSON que se devuelve de un cultivo
public record CultivoResponse(
        // Id del cultivo
        Long id,
        // Nombre
        String nombre,
        // Descripción
        String descripcion,
        // Solo el id del sector (no el sector completo) para que el JSON sea simple
        Long sectorId,
        // Fecha de siembra
        LocalDate fechaSiembra,
        // Fecha de trasplante (null si no tiene)
        LocalDate fechaTrasplante,
        // Fecha estimada de cosecha
        LocalDate fechaCosechaEstimada,
        // Frecuencia de riego en días
        int frecuenciaRiegoDias,
        // Estado
        EstadoCultivo estado,
        // Observaciones
        String observaciones
) {
    // Convierte la entidad en DTO. c.getSector().getId() saca el id del sector relacionado
    public static CultivoResponse desde(Cultivo c) {
        return new CultivoResponse(c.getId(), c.getNombre(), c.getDescripcion(), c.getSector().getId(),
                c.getFechaSiembra(), c.getFechaTrasplante(), c.getFechaCosechaEstimada(),
                c.getFrecuenciaRiegoDias(), c.getEstado(), c.getObservaciones());
    }
}
