package cl.duoc.mshuerto.controller;

import cl.duoc.mshuerto.dto.SectorResponse;
import cl.duoc.mshuerto.model.EstadoSector;
import cl.duoc.mshuerto.service.CultivoService;
import cl.duoc.mshuerto.service.SectorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* PRUEBAS DE LA CAPA HTTP DE SECTORES (MockMvc, sin base de datos) */
// @WebMvcTest levanta solo la capa web (controlador + ManejadorErrores), sin MySQL
@WebMvcTest(SectorController.class)
class SectorControllerTest {

    // Simula peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // Servicio falso dentro de Spring
    @MockitoBean
    private SectorService sectorService;

    // También se reemplaza porque el controlador lo necesita en su constructor
    @MockitoBean
    private CultivoService cultivoService;

    // Prueba: GET /api/sectores → 200 con la lista en JSON
    @Test
    void listarSectores_responde200ConLaLista() throws Exception {
        LocalDateTime ahora = LocalDateTime.of(2026, 11, 20, 10, 0);
        when(sectorService.listar()).thenReturn(List.of(
                new SectorResponse(1L, "S-01", "Bancal Norte", "Bancal elevado", "Patio", EstadoSector.EN_USO, 1L, ahora, ahora)
        ));

        mockMvc.perform(get("/api/sectores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("S-01"))
                .andExpect(jsonPath("$[0].estado").value("EN_USO"));
    }

    // Prueba: código con formato inválido → 400 por @Pattern, sin llegar al servicio
    @Test
    void crearConCodigoInvalido_responde400YNoLlamaAlServicio() throws Exception {
        // Text block de Java (""" ... """): permite escribir el JSON en varias líneas
        String json = """
                {"codigo":"SECTOR1","nombre":"Bancal Sur","descripcion":"Bancal de flores",
                 "ubicacion":"Gimnasio","estado":"EN_USO","responsableId":null}
                """;

        mockMvc.perform(post("/api/sectores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de código inválido. Ejemplo: S-01"));

        verify(sectorService, never()).crear(any());
    }

    // Prueba: estado que no existe en el enum → 400 (JSON inválido)
    @Test
    void crearConEstadoInexistente_responde400() throws Exception {
        String json = """
                {"codigo":"S-09","nombre":"Bancal Sur","descripcion":"Bancal de flores",
                 "ubicacion":"Gimnasio","estado":"TERMINADO"}
                """;

        mockMvc.perform(post("/api/sectores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }
}
