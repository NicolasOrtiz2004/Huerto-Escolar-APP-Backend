package cl.duoc.mshuerto.config;

import cl.duoc.mshuerto.model.Cultivo;
import cl.duoc.mshuerto.model.EstadoCultivo;
import cl.duoc.mshuerto.model.EstadoSector;
import cl.duoc.mshuerto.model.Sector;
import cl.duoc.mshuerto.repository.CultivoRepository;
import cl.duoc.mshuerto.repository.SectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/*
 * DATOS INICIALES FICTICIOS
 * Los mismos 4 sectores y 6 cultivos que trae la app (DatosIniciales.kt),
 * insertados EN EL MISMO ORDEN para que los id coincidan con los de Room
 * (las tareas guardadas en el teléfono apuntan a esos id).
 * Responsables: 1 = docente, 2 = tallerista (ids de ms-usuarios).
 * Las fechas son relativas a HOY, igual que en la app.
 */
// Se ejecuta una vez al arrancar (CommandLineRunner)
@Component
public class DatosIniciales implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatosIniciales.class);
    // Ids de ms-usuarios: 1 = docente, 2 = tallerista
    private static final long DOCENTE = 1L;
    private static final long TALLERISTA = 2L;

    // Para insertar los sectores
    private final SectorRepository sectorRepository;
    // Para insertar los cultivos
    private final CultivoRepository cultivoRepository;

    public DatosIniciales(SectorRepository sectorRepository, CultivoRepository cultivoRepository) {
        this.sectorRepository = sectorRepository;
        this.cultivoRepository = cultivoRepository;
    }

    // Método de la interfaz CommandLineRunner
    @Override
    public void run(String... args) {
        // Si ya hay datos, no se vuelve a cargar nada
        if (sectorRepository.count() > 0) {
            return;
        }
        // Las fechas se calculan relativas a hoy (hoy.minusDays(25), hoy.plusDays(10)...)
        LocalDate hoy = LocalDate.now();

        // ------------------------- SECTORES (id 1 a 4) -------------------------
        Sector s1 = new Sector("S-01", "Bancal Norte", "Bancal elevado de madera para hortalizas de hoja", "Junto a la sala de 1° básico", EstadoSector.EN_USO, DOCENTE);
        Sector s2 = new Sector("S-02", "Invernadero", "Pequeño invernadero de plástico para almácigos y tomates", "Patio trasero", EstadoSector.EN_USO, TALLERISTA);
        Sector s3 = new Sector("S-03", "Hierbas aromáticas", "Maceteros con hierbas y plantas aromáticas", "Pasillo central", EstadoSector.EN_USO, DOCENTE);
        Sector s4 = new Sector("S-04", "Compostera", "Zona de compost y preparación de tierra", "Esquina sur del huerto", EstadoSector.EN_MANTENIMIENTO, TALLERISTA);
        sectorRepository.saveAll(List.of(s1, s2, s3, s4));

        // ------------------------- CULTIVOS (id 1 a 6) -------------------------
        cultivoRepository.saveAll(List.of(
                new Cultivo("Lechuga", "Lechuga escarola sembrada por 6° básico", s1, hoy.minusDays(25), hoy.minusDays(10), hoy.plusDays(10), 2, EstadoCultivo.EN_CRECIMIENTO, "Buen crecimiento"),
                new Cultivo("Zanahoria", "Zanahorias en surcos", s1, hoy.minusDays(40), null, hoy.plusDays(30), 3, EstadoCultivo.EN_CRECIMIENTO, ""),
                new Cultivo("Tomate cherry", "Tomates tutorados con caña", s2, hoy.minusDays(60), hoy.minusDays(35), hoy.plusDays(5), 1, EstadoCultivo.LISTO_PARA_COSECHAR, "Primeros frutos rojos"),
                new Cultivo("Almácigo de acelga", "Bandejas de almácigo", s2, hoy.plusDays(3), null, hoy.plusDays(70), 1, EstadoCultivo.PLANIFICADO, ""),
                new Cultivo("Albahaca", "Albahaca en maceteros", s3, hoy.minusDays(20), null, hoy.plusDays(20), 2, EstadoCultivo.CON_PROBLEMAS, "Hojas con manchas"),
                new Cultivo("Cilantro", "Cilantro para la feria escolar", s3, hoy.minusDays(15), null, hoy.plusDays(25), 2, EstadoCultivo.SEMBRADO, "")
        ));
        log.info("ms-huerto: se cargaron 4 sectores y 6 cultivos de prueba");
    }
}
