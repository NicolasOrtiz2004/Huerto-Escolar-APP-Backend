package cl.duoc.mssensores.service;

import cl.duoc.mssensores.dto.LecturaRequest;
import cl.duoc.mssensores.dto.LecturaResponse;
import cl.duoc.mssensores.model.EstadoLectura;
import cl.duoc.mssensores.model.LecturaSensor;
import cl.duoc.mssensores.repository.LecturaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/* PRUEBAS UNITARIAS DEL SERVICIO DE LECTURAS SIMULADAS */
// Activa Mockito
@ExtendWith(MockitoExtension.class)
class LecturaServiceTest {

    // Repositorio falso (no usa MySQL)
    @Mock
    private LecturaRepository lecturaRepository;

    // Servicio real que se prueba
    @InjectMocks
    private LecturaService lecturaService;

    // Prueba: los límites exactos (29.9, 30, 80, 80.1) quedan en el estado correcto
    @Test
    void clasificar_respetaLosUmbralesDeHumedad() {
        assertEquals(EstadoLectura.BAJO, LecturaService.clasificar(29.9));
        assertEquals(EstadoLectura.NORMAL, LecturaService.clasificar(30));
        assertEquals(EstadoLectura.NORMAL, LecturaService.clasificar(80));
        assertEquals(EstadoLectura.ALTO, LecturaService.clasificar(80.1));
    }

    // Prueba: 100 lecturas aleatorias quedan dentro de los rangos y con origen simulado
    @Test
    void lecturaAleatoria_quedaEnRangosRealistasYMarcadaComoSimulada() {
        Random random = new Random(2026);
        for (int i = 0; i < 100; i++) {
            LecturaSensor l = LecturaService.crearLecturaAleatoria(1L, LocalDateTime.now(), random);
            assertTrue(l.getHumedadSuelo() >= 15 && l.getHumedadSuelo() <= 90);
            assertTrue(l.getTemperatura() >= 8 && l.getTemperatura() <= 32);
            assertEquals(LecturaSensor.ORIGEN_SIMULADO, l.getOrigen());
            assertEquals(LecturaService.clasificar(l.getHumedadSuelo()), l.getEstado()); // estado coherente
        }
    }

    // Prueba: si mandan el sector 2 repetido, se genera una sola lectura para él
    @Test
    void simular_generaUnaLecturaPorSectorSinRepetir() {
        // saveAll devuelve la misma lista que recibe
        when(lecturaRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<LecturaResponse> resultado = lecturaService.simular(List.of(1L, 2L, 2L, 3L));

        assertEquals(3, resultado.size()); // el 2 repetido cuenta una sola vez
        assertEquals(List.of(1L, 2L, 3L), resultado.stream().map(LecturaResponse::sectorId).toList());
    }

    // Prueba: lectura manual → estado calculado (BAJO) y valor redondeado a 1 decimal
    @Test
    void registrarManual_calculaElEstadoYRedondea() {
        when(lecturaRepository.save(any(LecturaSensor.class))).thenAnswer(inv -> inv.getArgument(0));

        LecturaResponse r = lecturaService.registrar(new LecturaRequest(2L, 22.46, 18.0, 55.0));

        assertEquals(EstadoLectura.BAJO, r.estado());
        assertEquals(22.5, r.humedadSuelo());
        assertEquals(LecturaService.ORIGEN_MANUAL, r.origen());
    }
}
