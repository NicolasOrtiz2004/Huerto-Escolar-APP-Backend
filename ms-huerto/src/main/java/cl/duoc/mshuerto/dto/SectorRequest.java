package cl.duoc.mshuerto.dto;

import cl.duoc.mshuerto.model.EstadoSector;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
 * JSON para crear o editar un sector (POST / PUT /api/sectores).
 * Las reglas son LAS MISMAS que util/Validaciones.kt de la app:
 * la app valida primero (respuesta inmediata) y el servidor vuelve a validar
 * (nunca se confía solo en lo que manda el cliente).
 *
 * Ejemplo:
 * { "codigo": "S-05", "nombre": "Bancal Sur", "descripcion": "Bancal de flores",
 *   "ubicacion": "Junto al gimnasio", "estado": "EN_USO", "responsableId": 1 }
 */
// record = clase inmutable con constructor y getters automáticos (codigo(), nombre()...)
public record SectorRequest(
        // Código: 1 a 3 letras, guion, 1 a 3 números
        @NotBlank(message = "El código es obligatorio")
        @Pattern(regexp = "^[A-Za-z]{1,3}-\\d{1,3}$", message = "Formato de código inválido. Ejemplo: S-01")
        String codigo,

        // Nombre del sector
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
        String nombre,

        // Descripción
        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 5, max = 200, message = "La descripción debe tener entre 5 y 200 caracteres")
        String descripcion,

        // Ubicación
        @NotBlank(message = "La ubicación es obligatoria")
        @Size(min = 3, max = 60, message = "La ubicación debe tener entre 3 y 60 caracteres")
        String ubicacion,

        // Estado (texto del enum: "EN_USO"...)
        @NotNull(message = "El estado es obligatorio")
        EstadoSector estado,

        Long responsableId // opcional
) {
}
