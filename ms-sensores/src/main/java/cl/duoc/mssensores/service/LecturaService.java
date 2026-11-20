package cl.duoc.mssensores.service;

import cl.duoc.mssensores.dto.LecturaRequest;
import cl.duoc.mssensores.dto.LecturaResponse;
import cl.duoc.mssensores.model.EstadoLectura;
import cl.duoc.mssensores.model.LecturaSensor;
import cl.duoc.mssensores.repository.LecturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/*
 * SERVICIO DE LECTURAS
 * Mismas reglas que SensorRepository de la app:
 *  - Humedad de suelo < 30 % → BAJO, > 80 % → ALTO, si no NORMAL.
 *  - Los valores aleatorios están en rangos realistas para un huerto en Santiago.
 *  - Nada de esto activa riego ni dispositivos: son datos de prueba.
 */
// Servicio con las reglas de las lecturas
@Service
public class LecturaService {

    // Texto de origen para lecturas ingresadas a mano
    static final String ORIGEN_MANUAL = "Ingreso manual de prueba";

    // Acceso a la tabla lecturas_sensor
    private final LecturaRepository lecturaRepository;
    // Generador de números aleatorios para las simulaciones
    private final Random random = new Random();

    // Inyección por constructor
    public LecturaService(LecturaRepository lecturaRepository) {
        this.lecturaRepository = lecturaRepository;
    }

    // Todas las lecturas, las más nuevas primero
    @Transactional(readOnly = true)
    public List<LecturaResponse> listar() {
        return lecturaRepository.findAllByOrderByFechaDesc().stream().map(LecturaResponse::desde).toList();
    }

    // Lecturas de un solo sector
    @Transactional(readOnly = true)
    public List<LecturaResponse> listarPorSector(Long sectorId) {
        return lecturaRepository.findBySectorIdOrderByFechaDesc(sectorId).stream().map(LecturaResponse::desde).toList();
    }

    /** Genera UNA lectura aleatoria por cada sector indicado y las guarda. */
    @Transactional
    public List<LecturaResponse> simular(List<Long> sectorIds) {
        // Todas las lecturas de esta simulación quedan con la misma hora
        LocalDateTime ahora = LocalDateTime.now();
        // Por cada id de sector se crea una lectura aleatoria
        List<LecturaSensor> nuevas = sectorIds.stream()
                .distinct() // si mandan el mismo sector dos veces, se genera una sola lectura
                .map(id -> crearLecturaAleatoria(id, ahora, random))
                .toList();
        // saveAll = INSERT de todas; se devuelven como DTO
        return lecturaRepository.saveAll(nuevas).stream().map(LecturaResponse::desde).toList();
    }

    /** Registro manual (o de un sensor real en el futuro). El estado se calcula, no se recibe. */
    @Transactional
    public LecturaResponse registrar(LecturaRequest request) {
        // Se redondean los valores y el estado se CALCULA con clasificar()
        LecturaSensor lectura = new LecturaSensor(
                request.sectorId(),
                LocalDateTime.now(),
                redondear(request.humedadSuelo()),
                redondear(request.temperatura()),
                redondear(request.humedadAmbiente()),
                ORIGEN_MANUAL,
                clasificar(request.humedadSuelo())
        );
        return LecturaResponse.desde(lecturaRepository.save(lectura));
    }

    // ----------------------------- Reglas (funciones puras) -----------------------------

    /** Umbrales de humedad de suelo para clasificar la lectura. */
    public static EstadoLectura clasificar(double humedadSuelo) {
        if (humedadSuelo < 30) return EstadoLectura.BAJO;
        if (humedadSuelo > 80) return EstadoLectura.ALTO;
        return EstadoLectura.NORMAL;
    }

    /** Crea una lectura ficticia. Recibe el Random para poder repetir valores (semilla fija en los datos iniciales). */
    public static LecturaSensor crearLecturaAleatoria(Long sectorId, LocalDateTime fecha, Random random) {
        // nextDouble(min, max) = número decimal aleatorio entre min y max
        double humedadSuelo = redondear(random.nextDouble(15.0, 90.0));
        return new LecturaSensor(
                sectorId,
                fecha,
                humedadSuelo,
                redondear(random.nextDouble(8.0, 32.0)),
                redondear(random.nextDouble(30.0, 85.0)),
                LecturaSensor.ORIGEN_SIMULADO,
                clasificar(humedadSuelo)
        );
    }

    /** 45.678 → 45.7 (un decimal) */
    static double redondear(double valor) {
        return Math.round(valor * 10.0) / 10.0;
    }
}
