package cl.duoc.mshuerto.controller;

import cl.duoc.mshuerto.dto.CultivoResponse;
import cl.duoc.mshuerto.dto.SectorRequest;
import cl.duoc.mshuerto.dto.SectorResponse;
import cl.duoc.mshuerto.service.CultivoService;
import cl.duoc.mshuerto.service.SectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * CONTROLADOR DE SECTORES
 *   GET  /api/sectores               → lista
 *   GET  /api/sectores/{id}          → uno
 *   GET  /api/sectores/{id}/cultivos → cultivos de ese sector
 *   POST /api/sectores               → crear (201)
 *   PUT  /api/sectores/{id}          → editar
 * (No hay DELETE: en el caso los sectores se marcan "Fuera de uso", no se borran.)
 */
// Atiende las rutas /api/sectores y responde JSON
// Prefijo de todas las rutas
@RestController
@RequestMapping("/api/sectores")
public class SectorController {

    // Servicio de sectores
    private final SectorService sectorService;
    // Servicio de cultivos (para /sectores/{id}/cultivos)
    private final CultivoService cultivoService;

    public SectorController(SectorService sectorService, CultivoService cultivoService) {
        this.sectorService = sectorService;
        this.cultivoService = cultivoService;
    }

    // GET /api/sectores
    @GetMapping
    public List<SectorResponse> listar() {
        return sectorService.listar();
    }

    // GET /api/sectores/1
    @GetMapping("/{id}")
    public SectorResponse buscar(@PathVariable Long id) {
        return sectorService.buscarPorId(id);
    }

    // GET /api/sectores/1/cultivos
    @GetMapping("/{id}/cultivos")
    public List<CultivoResponse> cultivosDelSector(@PathVariable Long id) {
        sectorService.buscarPorId(id); // 404 si el sector no existe
        return cultivoService.listar(id);
    }

    // POST /api/sectores → 201 Created
    @PostMapping
    public ResponseEntity<SectorResponse> crear(@Valid @RequestBody SectorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectorService.crear(request));
    }

    // PUT /api/sectores/1 → reemplaza los datos del sector
    @PutMapping("/{id}")
    public SectorResponse actualizar(@PathVariable Long id, @Valid @RequestBody SectorRequest request) {
        return sectorService.actualizar(id, request);
    }
}
