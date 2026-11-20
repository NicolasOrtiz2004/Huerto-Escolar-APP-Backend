package cl.duoc.mshuerto.dto;

import cl.duoc.mshuerto.model.EstadoSector;
import cl.duoc.mshuerto.model.Sector;

import java.time.LocalDateTime;

/** Lo que devuelve el microservicio de un sector (las fechas salen como "2026-11-20T12:30:00"). */
// Cada componente del record se convierte en un campo del JSON de respuesta
public record SectorResponse(
        // Id asignado por MySQL (la app lo usa como id en Room)
        Long id,
        // Código del sector
        String codigo,
        // Nombre
        String nombre,
        // Descripción
        String descripcion,
        // Ubicación
        String ubicacion,
        // Estado
        EstadoSector estado,
        // Id del responsable en ms-usuarios (puede ser null)
        Long responsableId,
        // Fecha de creación ("2026-11-20T12:30:00")
        LocalDateTime fechaCreacion,
        // Fecha del último cambio
        LocalDateTime fechaActualizacion
) {
    // Convierte la entidad (tabla) en el DTO (JSON)
    public static SectorResponse desde(Sector s) {
        return new SectorResponse(s.getId(), s.getCodigo(), s.getNombre(), s.getDescripcion(), s.getUbicacion(),
                s.getEstado(), s.getResponsableId(), s.getFechaCreacion(), s.getFechaActualizacion());
    }
}
