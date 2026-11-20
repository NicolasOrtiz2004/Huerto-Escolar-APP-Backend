package cl.duoc.mshuerto.service;

import cl.duoc.mshuerto.dto.CultivoRequest;
import cl.duoc.mshuerto.dto.CultivoResponse;
import cl.duoc.mshuerto.exception.ApiException;
import cl.duoc.mshuerto.model.Cultivo;
import cl.duoc.mshuerto.model.EstadoCultivo;
import cl.duoc.mshuerto.model.EstadoSector;
import cl.duoc.mshuerto.model.Sector;
import cl.duoc.mshuerto.repository.CultivoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* PRUEBAS UNITARIAS DE LAS REGLAS DE CULTIVOS */
// Activa Mockito
@ExtendWith(MockitoExtension.class)
class CultivoServiceTest {

    // Repositorio falso
    @Mock
    private CultivoRepository cultivoRepository;

    // SectorService falso: decidimos qué sector devuelve
    @Mock
    private SectorService sectorService;

    // Servicio real que se prueba
    @InjectMocks
    private CultivoService cultivoService;

    // Fecha fija: las pruebas dan el mismo resultado cualquier día
    private static final LocalDate HOY = LocalDate.of(2026, 11, 20);

    // Ayuda: crea un sector de prueba con el estado indicado
    private Sector sector(EstadoSector estado) {
        Sector s = new Sector("S-01", "Bancal Norte", "Bancal elevado", "Patio", estado, 1L);
        s.setId(1L);
        return s;
    }

    // Ayuda: crea un CultivoRequest con las fechas indicadas
    private CultivoRequest request(LocalDate siembra, LocalDate trasplante, LocalDate cosecha) {
        return new CultivoRequest("Lechuga", "Lechuga escarola", 1L, siembra, trasplante, cosecha, 2, EstadoCultivo.SEMBRADO, "");
    }

    // Prueba: cosecha antes de la siembra → 400 y no se guarda
    @Test
    void cosechaAntesDeLaSiembra_lanza400() {
        when(sectorService.obtenerEntidad(1L)).thenReturn(sector(EstadoSector.EN_USO));

        ApiException error = assertThrows(ApiException.class,
                () -> cultivoService.crear(request(HOY, null, HOY.minusDays(1))));

        assertEquals(HttpStatus.BAD_REQUEST, error.getEstado());
        assertEquals("La cosecha no puede ser anterior a la siembra", error.getMessage());
        verify(cultivoRepository, never()).save(any());
    }

    // Prueba: trasplante después de la cosecha → 400
    @Test
    void trasplanteDespuesDeLaCosecha_lanza400() {
        when(sectorService.obtenerEntidad(1L)).thenReturn(sector(EstadoSector.EN_USO));

        ApiException error = assertThrows(ApiException.class,
                () -> cultivoService.crear(request(HOY, HOY.plusDays(40), HOY.plusDays(30))));

        assertEquals("El trasplante no puede ser posterior a la cosecha", error.getMessage());
    }

    // Prueba: sector fuera de uso → 409
    @Test
    void sectorFueraDeUso_lanza409() {
        when(sectorService.obtenerEntidad(1L)).thenReturn(sector(EstadoSector.FUERA_DE_USO));

        ApiException error = assertThrows(ApiException.class,
                () -> cultivoService.crear(request(HOY, null, HOY.plusDays(30))));

        assertEquals(HttpStatus.CONFLICT, error.getEstado());
    }

    // Prueba: cultivo válido → se guarda y la respuesta trae el id del sector
    @Test
    void cultivoValido_seGuardaConSuSector() {
        when(sectorService.obtenerEntidad(1L)).thenReturn(sector(EstadoSector.EN_USO));
        when(cultivoRepository.save(any(Cultivo.class))).thenAnswer(inv -> {
            Cultivo c = inv.getArgument(0);
            c.setId(7L);
            return c;
        });

        CultivoResponse respuesta = cultivoService.crear(request(HOY, HOY.plusDays(10), HOY.plusDays(30)));

        assertEquals(7L, respuesta.id());
        assertEquals(1L, respuesta.sectorId());
        assertEquals(HOY.plusDays(30), respuesta.fechaCosechaEstimada());
    }
}
