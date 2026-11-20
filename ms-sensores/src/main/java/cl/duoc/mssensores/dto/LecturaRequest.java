package cl.duoc.mssensores.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/*
 * Registro MANUAL de una lectura (POST /api/lecturas).
 * Es el punto de entrada que usaría un sensor real en el futuro.
 * Se validan rangos físicamente posibles.
 *
 * { "sectorId": 1, "humedadSuelo": 45.5, "temperatura": 18.2, "humedadAmbiente": 60 }
 */
// Datos de una lectura manual; @DecimalMin/@DecimalMax validan el rango
public record LecturaRequest(
        // Sector de la lectura
        @NotNull(message = "Debes indicar el sector")
        Long sectorId,

        // Double (objeto) para que @NotNull detecte si falta
        @NotNull(message = "La humedad del suelo es obligatoria")
        @DecimalMin(value = "0", message = "La humedad del suelo va de 0 a 100 %")
        @DecimalMax(value = "100", message = "La humedad del suelo va de 0 a 100 %")
        Double humedadSuelo,

        // Temperatura en °C
        @NotNull(message = "La temperatura es obligatoria")
        @DecimalMin(value = "-10", message = "La temperatura debe estar entre -10 y 60 °C")
        @DecimalMax(value = "60", message = "La temperatura debe estar entre -10 y 60 °C")
        Double temperatura,

        // Humedad ambiente en %
        @NotNull(message = "La humedad ambiente es obligatoria")
        @DecimalMin(value = "0", message = "La humedad ambiente va de 0 a 100 %")
        @DecimalMax(value = "100", message = "La humedad ambiente va de 0 a 100 %")
        Double humedadAmbiente
) {
}
