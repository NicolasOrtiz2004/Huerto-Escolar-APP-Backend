package cl.duoc.msusuarios.controller;

import cl.duoc.msusuarios.dto.UsuarioRequest;
import cl.duoc.msusuarios.dto.UsuarioResponse;
import cl.duoc.msusuarios.service.UsuarioService;
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
 * CONTROLADOR DE USUARIOS
 *   GET  /api/usuarios       → lista de cuentas (la app la sincroniza en Room)
 *   GET  /api/usuarios/{id}  → una cuenta (lo usa ms-huerto para validar responsables)
 *   POST /api/usuarios       → crear cuenta (201 Created)
 */
// Clase que atiende las rutas /api/usuarios y responde JSON
// Prefijo de todas las rutas de esta clase
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // Servicio con las reglas de negocio (inyectado por constructor)
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET /api/usuarios
    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listar();
    }

    // GET /api/usuarios/5 → @PathVariable toma el 5 de la URL
    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    // POST /api/usuarios con el JSON en el cuerpo
    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        // Se responde 201 Created (código correcto al crear algo) con el usuario creado
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(request));
    }
}
