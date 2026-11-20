package cl.duoc.mshuerto.dto;

import cl.duoc.mshuerto.model.EstadoCultivo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/*
 * JSON para crear o editar un cultivo (POST / PUT /api/cultivos).
 * Las fechas viajan como texto ISO: "2026-11-20".
 *
 * Ejemplo:
 * { "nombre": "Lechuga", "descripcion": "Lechuga escarola", "sectorId": 1,
 *   "fechaSiembra": "2026-11-01", "fechaTrasplante": null,
 *   "fechaCosechaEstimada": "2026-12-15", "frecuenciaRiegoDias": 2,
 *   "estado": "SEMBRADO", "observaciones": "" }
 */
// Las anotaciones validan el JSON antes de llegar al servicio (si falla → 400)
public record CultivoRequest(
        // Nombre de la planta
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
        String nombre,

        // Descripción
        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
        String descripcion,

        // Id del sector al que pertenece
        @NotNull(message = "Debes indicar el sector")
        Long sectorId,

        // Fecha de siembra ("2026-11-01")
        @NotNull(message = "La fecha de siembra es obligatoria")
        LocalDate fechaSiembra,

        LocalDate fechaTrasplante, // opcional

        // Fecha estimada de cosecha
        @NotNull(message = "La fecha estimada de cosecha es obligatoria")
        LocalDate fechaCosechaEstimada,

        // Integer (y no int) para que @NotNull detecte si no lo mandaron
        @NotNull(message = "La frecuencia de riego es obligatoria")
        @Min(value = 1, message = "La frecuencia debe ser mayor a 0")
        @Max(value = 30, message = "La frecuencia no puede superar 30 días")
        Integer frecuenciaRiegoDias,

        // Estado del cultivo
        @NotNull(message = "El estado es obligatorio")
        EstadoCultivo estado,

        @Size(max = 300, message = "Máximo 300 caracteres en las observaciones")
        String observaciones // opcional
) {
}
