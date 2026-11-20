package cl.duoc.mshuerto.service;

import cl.duoc.mshuerto.dto.CultivoRequest;
import cl.duoc.mshuerto.dto.CultivoResponse;
import cl.duoc.mshuerto.exception.ApiException;
import cl.duoc.mshuerto.model.Cultivo;
import cl.duoc.mshuerto.model.EstadoSector;
import cl.duoc.mshuerto.model.Sector;
import cl.duoc.mshuerto.repository.CultivoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * SERVICIO DE CULTIVOS — mismas reglas que SectorRepository.guardarCultivo de la app:
 *  - El cultivo debe pertenecer a un sector EXISTENTE.
 *  - No se registran cultivos en un sector FUERA_DE_USO.
 *  - La cosecha no puede ser antes que la siembra.
 *  - El trasplante (opcional) debe quedar entre la siembra y la cosecha.
 */
// Servicio con las reglas de los cultivos
@Service
public class CultivoService {

    // Acceso a la tabla cultivos
    private final CultivoRepository cultivoRepository;
    // Se reutiliza SectorService para buscar el sector (y su 404)
    private final SectorService sectorService;

    // Inyección por constructor
    public CultivoService(CultivoRepository cultivoRepository, SectorService sectorService) {
        this.cultivoRepository = cultivoRepository;
        this.sectorService = sectorService;
    }

    // Si sectorId es null → todos; si viene → solo los de ese sector
    @Transactional(readOnly = true)
    public List<CultivoResponse> listar(Long sectorId) {
        // Operador ternario: condición ? valor_si_true : valor_si_false
        List<Cultivo> cultivos = (sectorId == null)
                ? cultivoRepository.findAllByOrderByNombreAsc()
                : cultivoRepository.findBySector_IdOrderByNombreAsc(sectorId);
        return cultivos.stream().map(CultivoResponse::desde).toList();
    }

    // Un cultivo por id (404 si no existe)
    @Transactional(readOnly = true)
    public CultivoResponse buscarPorId(Long id) {
        return CultivoResponse.desde(obtenerEntidad(id));
    }

    // Crea un cultivo nuevo
    @Transactional
    public CultivoResponse crear(CultivoRequest request) {
        // 1) El sector debe existir y no estar fuera de uso
        Sector sector = sectorValido(request.sectorId());
        // 2) Las fechas deben ser coherentes
        validarFechas(request);
        // 3) Se arma la entidad y se guarda
        Cultivo cultivo = new Cultivo(
                request.nombre().trim(),
                request.descripcion().trim(),
                sector,
                request.fechaSiembra(),
                request.fechaTrasplante(),
                request.fechaCosechaEstimada(),
                request.frecuenciaRiegoDias(),
                request.estado(),
                limpiar(request.observaciones())
        );
        return CultivoResponse.desde(cultivoRepository.save(cultivo));
    }

    // Edita un cultivo existente (mismas reglas que al crear)
    @Transactional
    public CultivoResponse actualizar(Long id, CultivoRequest request) {
        Cultivo cultivo = obtenerEntidad(id);
        Sector sector = sectorValido(request.sectorId());
        validarFechas(request);
        cultivo.setNombre(request.nombre().trim());
        cultivo.setDescripcion(request.descripcion().trim());
        cultivo.setSector(sector);
        cultivo.setFechaSiembra(request.fechaSiembra());
        cultivo.setFechaTrasplante(request.fechaTrasplante());
        cultivo.setFechaCosechaEstimada(request.fechaCosechaEstimada());
        cultivo.setFrecuenciaRiegoDias(request.frecuenciaRiegoDias());
        cultivo.setEstado(request.estado());
        cultivo.setObservaciones(limpiar(request.observaciones()));
        return CultivoResponse.desde(cultivoRepository.save(cultivo));
    }

    /**
     * La app revisa ANTES (en Room) que el cultivo no tenga tareas ni reportes;
     * si los tiene, no llama a este endpoint y pide marcarlo "Finalizado".
     */
    // Borra un cultivo (404 si no existe)
    @Transactional
    public void eliminar(Long id) {
        Cultivo cultivo = obtenerEntidad(id);
        cultivoRepository.delete(cultivo);
    }

    // ----------------------------- Reglas -----------------------------

    // Busca el sector y aplica la regla de "fuera de uso"
    private Sector sectorValido(Long sectorId) {
        Sector sector = sectorService.obtenerEntidad(sectorId); // 404 si no existe
        if (sector.getEstado() == EstadoSector.FUERA_DE_USO) {
            throw new ApiException(HttpStatus.CONFLICT, "No se pueden registrar cultivos en un sector fuera de uso");
        }
        return sector;
    }

    /** Reglas de fechas (las mismas de validarFechaCosecha / validarFechaTrasplante en la app). */
    // isBefore / isAfter comparan fechas LocalDate
    void validarFechas(CultivoRequest r) {
        if (r.fechaCosechaEstimada().isBefore(r.fechaSiembra())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La cosecha no puede ser anterior a la siembra");
        }
        if (r.fechaTrasplante() != null) {
            if (r.fechaTrasplante().isBefore(r.fechaSiembra())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El trasplante no puede ser anterior a la siembra");
            }
            if (r.fechaTrasplante().isAfter(r.fechaCosechaEstimada())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "El trasplante no puede ser posterior a la cosecha");
            }
        }
    }

    // Busca el cultivo o lanza 404
    private Cultivo obtenerEntidad(Long id) {
        return cultivoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe un cultivo con id " + id));
    }

    // Observaciones: null → "" y sin espacios a los lados
    private String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
