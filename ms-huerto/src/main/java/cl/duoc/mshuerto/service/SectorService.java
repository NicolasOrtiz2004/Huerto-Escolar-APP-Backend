package cl.duoc.mshuerto.service;

import cl.duoc.mshuerto.client.UsuarioClient;
import cl.duoc.mshuerto.dto.SectorRequest;
import cl.duoc.mshuerto.dto.SectorResponse;
import cl.duoc.mshuerto.exception.ApiException;
import cl.duoc.mshuerto.model.Sector;
import cl.duoc.mshuerto.repository.SectorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/*
 * SERVICIO DE SECTORES — reglas de negocio:
 *  - El código del sector no se repite (S-01, S-02...).
 *  - El código se guarda en MAYÚSCULAS.
 *  - Si hay responsable, se verifica en ms-usuarios que sea un admin activo.
 */
// Servicio: aquí van las reglas de negocio de los sectores
@Service
public class SectorService {

    // Acceso a la tabla sectores
    private final SectorRepository sectorRepository;
    // Cliente para preguntarle a ms-usuarios por el responsable
    private final UsuarioClient usuarioClient;

    // Inyección por constructor: Spring entrega las dos dependencias
    public SectorService(SectorRepository sectorRepository, UsuarioClient usuarioClient) {
        this.sectorRepository = sectorRepository;
        this.usuarioClient = usuarioClient;
    }

    // Todos los sectores ordenados por código
    @Transactional(readOnly = true)
    public List<SectorResponse> listar() {
        return sectorRepository.findAllByOrderByCodigoAsc().stream()
                .map(SectorResponse::desde)
                .toList();
    }

    // Un sector por id (404 si no existe)
    @Transactional(readOnly = true)
    public SectorResponse buscarPorId(Long id) {
        return SectorResponse.desde(obtenerEntidad(id));
    }

    // Crea un sector nuevo
    @Transactional
    public SectorResponse crear(SectorRequest request) {
        // Se normaliza el código: sin espacios y en MAYÚSCULAS ("s-01" → "S-01")
        String codigo = request.codigo().trim().toUpperCase();
        // Regla: el código no se repite → 409
        if (sectorRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un sector con el código " + codigo);
        }
        if (request.responsableId() != null) {
            usuarioClient.verificarResponsable(request.responsableId()); // llamada a ms-usuarios
        }
        // Se arma la entidad nueva
        Sector sector = new Sector(
                codigo,
                request.nombre().trim(),
                request.descripcion().trim(),
                request.ubicacion().trim(),
                request.estado(),
                request.responsableId()
        );
        // save() = INSERT; devuelve el sector con su id
        return SectorResponse.desde(sectorRepository.save(sector));
    }

    // Edita un sector existente
    @Transactional
    public SectorResponse actualizar(Long id, SectorRequest request) {
        // 404 si no existe
        Sector sector = obtenerEntidad(id);
        String codigo = request.codigo().trim().toUpperCase();
        // El código no puede ser el de OTRO sector
        if (sectorRepository.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un sector con el código " + codigo);
        }
        // Solo se consulta a ms-usuarios si CAMBIÓ el responsable (menos llamadas por la red)
        boolean cambioResponsable = !Objects.equals(request.responsableId(), sector.getResponsableId());
        if (request.responsableId() != null && cambioResponsable) {
            usuarioClient.verificarResponsable(request.responsableId());
        }
        // Se cambian los campos del objeto existente...
        sector.setCodigo(codigo);
        sector.setNombre(request.nombre().trim());
        sector.setDescripcion(request.descripcion().trim());
        sector.setUbicacion(request.ubicacion().trim());
        sector.setEstado(request.estado());
        sector.setResponsableId(request.responsableId());
        sector.setFechaActualizacion(LocalDateTime.now());
        // ...y save() sobre un objeto con id = UPDATE
        return SectorResponse.desde(sectorRepository.save(sector));
    }

    /** Lo usa también CultivoService para comprobar que el sector existe. */
    Sector obtenerEntidad(Long id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe un sector con id " + id));
    }
}
