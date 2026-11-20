package cl.duoc.mssensores.controller;

import cl.duoc.mssensores.dto.LecturaRequest;
import cl.duoc.mssensores.dto.LecturaResponse;
import cl.duoc.mssensores.dto.SimulacionRequest;
import cl.duoc.mssensores.service.LecturaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * CONTROLADOR DE LECTURAS
 *   GET  /api/lecturas                    → todas (más nuevas primero)
 *   GET  /api/lecturas/sector/{sectorId}  → de un sector
 *   POST /api/lecturas/simular            → genera una lectura aleatoria por sector (201)
 *   POST /api/lecturas                    → registra una lectura manual (201)
 */
// Atiende las rutas /api/lecturas y responde JSON
// Prefijo de todas las rutas
@RestController
@RequestMapping("/api/lecturas")
public class LecturaController {

    // Servicio de lecturas
    private final LecturaService lecturaService;

    public LecturaController(LecturaService lecturaService) {
        this.lecturaService = lecturaService;
    }

    // GET /api/lecturas
    @GetMapping
    public List<LecturaResponse> listar() {
        return lecturaService.listar();
    }

    // GET /api/lecturas/sector/1
    @GetMapping("/sector/{sectorId}")
    public List<LecturaResponse> listarPorSector(@PathVariable Long sectorId) {
        return lecturaService.listarPorSector(sectorId);
    }

    // POST /api/lecturas/simular → 201 con las lecturas creadas
    @PostMapping("/simular")
    public ResponseEntity<List<LecturaResponse>> simular(@Valid @RequestBody SimulacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturaService.simular(request.sectorIds()));
    }

    // POST /api/lecturas → registra una lectura manual (201)
    @PostMapping
    public ResponseEntity<LecturaResponse> registrar(@Valid @RequestBody LecturaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturaService.registrar(request));
    }
}
