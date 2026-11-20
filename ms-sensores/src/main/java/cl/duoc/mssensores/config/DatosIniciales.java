package cl.duoc.mssensores.config;

import cl.duoc.mssensores.model.LecturaSensor;
import cl.duoc.mssensores.repository.LecturaRepository;
import cl.duoc.mssensores.service.LecturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * DATOS INICIALES: 3 lecturas simuladas (hace 2 días, ayer y hoy) para cada
 * uno de los 4 sectores de prueba. Random con semilla fija (2026) → siempre
 * los mismos valores de demostración, igual que en la app.
 */
// Se ejecuta una vez al arrancar (CommandLineRunner)
@Component
public class DatosIniciales implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatosIniciales.class);

    // Para insertar las lecturas de prueba
    private final LecturaRepository lecturaRepository;

    public DatosIniciales(LecturaRepository lecturaRepository) {
        this.lecturaRepository = lecturaRepository;
    }

    // Método de la interfaz CommandLineRunner
    @Override
    public void run(String... args) {
        // Si ya hay lecturas, no se carga nada
        if (lecturaRepository.count() > 0) {
            return;
        }
        // Semilla fija (2026): los "aleatorios" salen siempre iguales
        Random random = new Random(2026);
        LocalDateTime ahora = LocalDateTime.now();
        List<LecturaSensor> lecturas = new ArrayList<>();
        // Sectores 1 a 4 × 3 días (hace 2 días, ayer y hoy) = 12 lecturas
        for (long sectorId = 1; sectorId <= 4; sectorId++) {
            for (int diasAtras = 2; diasAtras >= 0; diasAtras--) {
                lecturas.add(LecturaService.crearLecturaAleatoria(sectorId, ahora.minusDays(diasAtras), random));
            }
        }
        // INSERT de las 12 lecturas
        lecturaRepository.saveAll(lecturas);
        log.info("ms-sensores: se cargaron {} lecturas simuladas de prueba", lecturas.size());
    }
}
