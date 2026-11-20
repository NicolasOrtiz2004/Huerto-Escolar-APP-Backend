package cl.duoc.mshuerto.controller;

import cl.duoc.mshuerto.dto.CultivoRequest;
import cl.duoc.mshuerto.dto.CultivoResponse;
import cl.duoc.mshuerto.service.CultivoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * CONTROLADOR DE CULTIVOS
 *   GET    /api/cultivos              → todos
 *   GET    /api/cultivos?sectorId=1   → filtrados por sector (@RequestParam opcional)
 *   GET    /api/cultivos/{id}         → uno
 *   POST   /api/cultivos              → crear (201)
 *   PUT    /api/cultivos/{id}         → editar
 *   DELETE /api/cultivos/{id}         → eliminar (204 No Content)
 */
// Atiende las rutas /api/cultivos y responde JSON
// Prefijo de todas las rutas
@RestController
@RequestMapping("/api/cultivos")
public class CultivoController {

    // Servicio de cultivos
    private final CultivoService cultivoService;

    public CultivoController(CultivoService cultivoService) {
        this.cultivoService = cultivoService;
    }

    // GET /api/cultivos o /api/cultivos?sectorId=1 (required = false → el filtro es opcional)
    @GetMapping
    public List<CultivoResponse> listar(@RequestParam(required = false) Long sectorId) {
        return cultivoService.listar(sectorId);
    }

    // GET /api/cultivos/1
    @GetMapping("/{id}")
    public CultivoResponse buscar(@PathVariable Long id) {
        return cultivoService.buscarPorId(id);
    }

    // POST /api/cultivos → 201 Created
    @PostMapping
    public ResponseEntity<CultivoResponse> crear(@Valid @RequestBody CultivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cultivoService.crear(request));
    }

    // PUT /api/cultivos/1
    @PutMapping("/{id}")
    public CultivoResponse actualizar(@PathVariable Long id, @Valid @RequestBody CultivoRequest request) {
        return cultivoService.actualizar(id, request);
    }

    // DELETE /api/cultivos/1 → 204 No Content (se borró y no hay nada que devolver)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cultivoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
