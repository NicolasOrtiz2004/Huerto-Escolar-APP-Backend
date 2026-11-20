package cl.duoc.mssensores.controller;

import cl.duoc.mssensores.dto.LecturaResponse;
import cl.duoc.mssensores.model.EstadoLectura;
import cl.duoc.mssensores.model.LecturaSensor;
import cl.duoc.mssensores.service.LecturaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* PRUEBAS DE LA CAPA HTTP DE LECTURAS */
// Levanta solo la capa web, sin MySQL
@WebMvcTest(LecturaController.class)
class LecturaControllerTest {

    // Simula peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // Servicio falso dentro de Spring
    @MockitoBean
    private LecturaService lecturaService;

    // Prueba: lista vacía → 400 por @NotEmpty y el servicio nunca se llama
    @Test
    void simularSinSectores_responde400() throws Exception {
        mockMvc.perform(post("/api/lecturas/simular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sectorIds\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Debes indicar al menos un sector"));

        verify(lecturaService, never()).simular(anyList());
    }

    // Prueba: lista con sectores → 201 y el JSON trae las 2 lecturas
    @Test
    void simularConSectores_responde201() throws Exception {
        when(lecturaService.simular(List.of(1L, 2L))).thenReturn(List.of(
                new LecturaResponse(20L, 1L, LocalDateTime.of(2026, 11, 20, 9, 0), 45.0, 18.0, 60.0, LecturaSensor.ORIGEN_SIMULADO, EstadoLectura.NORMAL),
                new LecturaResponse(21L, 2L, LocalDateTime.of(2026, 11, 20, 9, 0), 25.0, 19.0, 58.0, LecturaSensor.ORIGEN_SIMULADO, EstadoLectura.BAJO)
        ));

        mockMvc.perform(post("/api/lecturas/simular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sectorIds\":[1,2]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].estado").value("BAJO"));
    }

    // Prueba: humedad 150 % → 400 por @DecimalMax
    @Test
    void lecturaManualConHumedadImposible_responde400() throws Exception {
        mockMvc.perform(post("/api/lecturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sectorId\":1,\"humedadSuelo\":150,\"temperatura\":20,\"humedadAmbiente\":50}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La humedad del suelo va de 0 a 100 %"));
    }

    // Prueba: GET /api/lecturas → 200
    @Test
    void listar_responde200() throws Exception {
        when(lecturaService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/lecturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
