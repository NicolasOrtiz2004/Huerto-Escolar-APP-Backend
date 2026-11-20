package cl.duoc.msusuarios.service;

import cl.duoc.msusuarios.dto.LoginRequest;
import cl.duoc.msusuarios.dto.UsuarioRequest;
import cl.duoc.msusuarios.dto.UsuarioResponse;
import cl.duoc.msusuarios.exception.ApiException;
import cl.duoc.msusuarios.model.Usuario;
import cl.duoc.msusuarios.repository.UsuarioRepository;
import cl.duoc.msusuarios.util.Seguridad;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * SERVICIO DE USUARIOS
 * ====================
 * Aquí van las REGLAS DE NEGOCIO (igual que el Repository de la app):
 * el controlador solo recibe la petición y le delega el trabajo a esta clase.
 *
 * Inyección por CONSTRUCTOR: Spring entrega el UsuarioRepository solo.
 * En las pruebas unitarias le pasamos un repositorio "falso" (Mockito).
 */
// @Service: Spring crea UN objeto de esta clase y lo entrega a quien lo pida (inyección de dependencias)
@Service
public class UsuarioService {

    /** Mensaje GENÉRICO a propósito: no revela qué nombres de usuario existen. */
    static final String CREDENCIALES_INVALIDAS = "Usuario o contraseña incorrectos";

    // final = se asigna una sola vez (en el constructor) y no cambia
    private final UsuarioRepository usuarioRepository;

    // Spring llama a este constructor y le pasa el repositorio automáticamente
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Inicio de sesión. Mismas reglas que tenía la app en local:
     * 1) el usuario debe existir, 2) la contraseña debe coincidir, 3) la cuenta debe estar activa.
     */
    // @Transactional: todo el método corre en UNA transacción de BD. readOnly = solo lectura (más rápido)
    @Transactional(readOnly = true)
    public UsuarioResponse login(LoginRequest request) {
        // Se normaliza: sin espacios a los lados y en minúsculas ("  Docente " → "docente")
        String nombreUsuario = request.nombreUsuario().trim().toLowerCase();

        // orElseThrow: si el Optional viene vacío (no existe) se lanza la excepción → 401
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, CREDENCIALES_INVALIDAS));

        // Regla 2: la contraseña debe coincidir con el hash guardado
        if (!Seguridad.passwordCorrecta(request.password(), usuario.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, CREDENCIALES_INVALIDAS);
        }
        // Regla 3: la cuenta debe estar activa → si no, 403 Forbidden
        if (!usuario.isActivo()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Tu cuenta está desactivada. Habla con tu docente");
        }
        // Se devuelve el DTO (sin el hash), no la entidad
        return UsuarioResponse.desde(usuario);
    }

    // Lista de todas las cuentas para que la app las guarde en Room
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        // stream().map(...).toList() = convierte cada Usuario en UsuarioResponse
        return usuarioRepository.findAllByOrderByNombreAsc().stream()
                .map(UsuarioResponse::desde)
                .toList();
    }

    // Busca una cuenta por id. Lo usa ms-huerto para validar responsables → 404 si no existe
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponse::desde)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe un usuario con id " + id));
    }

    /** Crea una cuenta nueva. El nombre de usuario no se puede repetir. */
    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        String nombreUsuario = request.nombreUsuario().trim().toLowerCase();
        // Regla: no se repiten nombres de usuario → 409 Conflict
        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe el usuario " + nombreUsuario);
        }
        // Se arma la entidad nueva (todavía sin id)
        Usuario nuevo = new Usuario(
                nombreUsuario,
                request.nombre().trim(),
                Seguridad.hashPassword(request.password()), // se guarda el HASH, nunca la contraseña
                request.rol(),
                request.taller().trim(),
                true
        );
        // save() hace el INSERT y devuelve el usuario con su id asignado por MySQL
        return UsuarioResponse.desde(usuarioRepository.save(nuevo));
    }
}
