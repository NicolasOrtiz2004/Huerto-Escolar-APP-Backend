package cl.duoc.msusuarios.controller;

import cl.duoc.msusuarios.dto.LoginRequest;
import cl.duoc.msusuarios.dto.UsuarioResponse;
import cl.duoc.msusuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * CONTROLADOR DE AUTENTICACIÓN
 *   POST /api/auth/login  → 200 con el usuario | 401 credenciales | 403 cuenta desactivada
 *
 * @Valid activa las validaciones del LoginRequest (@NotBlank).
 * @RequestBody convierte el JSON del cuerpo en el objeto Java.
 */
// @RestController = esta clase atiende peticiones HTTP y responde JSON automáticamente
// Prefijo común de todas las rutas de esta clase
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // El controlador NO tiene lógica: todo lo delega al servicio
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // @PostMapping = responde a peticiones POST en /api/auth/login
    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest request) {
        // Si todo sale bien → 200 OK con el usuario en JSON. Si falla, el servicio lanza ApiException
        return usuarioService.login(request);
    }
}
