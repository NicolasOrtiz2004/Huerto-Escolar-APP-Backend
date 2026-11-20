package cl.duoc.mssensores.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * La app pide "genera una lectura para estos sectores" (los que no están fuera de uso):
 *   POST /api/lecturas/simular   { "sectorIds": [1, 2, 3] }
 */
// Lista de sectores para los que se generará una lectura
public record SimulacionRequest(
        // @NotEmpty = la lista no puede venir vacía · @NotNull dentro de <> = ningún elemento puede ser null
        @NotEmpty(message = "Debes indicar al menos un sector")
        @Size(max = 50, message = "Máximo 50 sectores por simulación")
        List<@NotNull(message = "El id de sector no puede ser nulo") Long> sectorIds
) {
}
