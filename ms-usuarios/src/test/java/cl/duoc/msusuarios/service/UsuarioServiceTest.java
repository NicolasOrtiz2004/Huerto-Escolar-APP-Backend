package cl.duoc.msusuarios.service;

import cl.duoc.msusuarios.dto.LoginRequest;
import cl.duoc.msusuarios.dto.UsuarioRequest;
import cl.duoc.msusuarios.dto.UsuarioResponse;
import cl.duoc.msusuarios.exception.ApiException;
import cl.duoc.msusuarios.model.Rol;
import cl.duoc.msusuarios.model.Usuario;
import cl.duoc.msusuarios.repository.UsuarioRepository;
import cl.duoc.msusuarios.util.Seguridad;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * PRUEBAS UNITARIAS DEL SERVICIO DE USUARIOS
 * ==========================================
 * No se levanta Spring ni MySQL: con Mockito creamos un UsuarioRepository
 * FALSO (@Mock) y decidimos qué responde con when(...).thenReturn(...).
 * Así probamos SOLO las reglas del servicio, en milisegundos.
 */
// Activa Mockito en JUnit 5: crea los @Mock y los inyecta en el @InjectMocks antes de cada prueba
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    // Repositorio FALSO: no toca MySQL; responde lo que digamos con when(...)
    @Mock
    private UsuarioRepository usuarioRepository;

    // El servicio REAL que estamos probando (con el repositorio falso adentro)
    @InjectMocks // crea el servicio y le "inyecta" el repositorio falso
    private UsuarioService usuarioService;

    // Ayuda: crea un docente de prueba con la contraseña "huerto123" (activo o no)
    private Usuario docente(boolean activo) {
        Usuario u = new Usuario("docente", "Prof. Marta Quiroga", Seguridad.hashPassword("huerto123"), Rol.ADMINISTRADOR, "Taller", activo);
        u.setId(1L);
        return u;
    }

    // Prueba: con usuario y clave correctos, el login devuelve el usuario
    @Test
    void loginCorrecto_devuelveElUsuarioSinHash() {
        // ARRANGE (preparar): cuando busquen "docente", el repositorio falso devuelve un docente activo
        when(usuarioRepository.findByNombreUsuario("docente")).thenReturn(Optional.of(docente(true)));

        // Con espacios y mayúsculas también funciona (se normaliza)
        // ACT (ejecutar) el método que se prueba
        UsuarioResponse respuesta = usuarioService.login(new LoginRequest("  Docente ", "huerto123"));

        // ASSERT (verificar): assertEquals(esperado, real) falla la prueba si no son iguales
        assertEquals(1L, respuesta.id());
        assertEquals(Rol.ADMINISTRADOR, respuesta.rol());
    }

    // Prueba: clave incorrecta → error 401 con el mensaje genérico
    @Test
    void loginConPasswordIncorrecta_lanza401() {
        when(usuarioRepository.findByNombreUsuario("docente")).thenReturn(Optional.of(docente(true)));

        // assertThrows comprueba que el código de la lambda LANCE esa excepción, y la devuelve para revisarla
        ApiException error = assertThrows(ApiException.class,
                () -> usuarioService.login(new LoginRequest("docente", "otraClave")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getEstado());
        assertEquals(UsuarioService.CREDENCIALES_INVALIDAS, error.getMessage());
    }

    // Prueba: usuario que no existe → mismo mensaje que clave incorrecta (no revela qué cuentas existen)
    @Test
    void loginConUsuarioInexistente_lanza401ConElMismoMensaje() {
        when(usuarioRepository.findByNombreUsuario("fantasma")).thenReturn(Optional.empty());

        ApiException error = assertThrows(ApiException.class,
                () -> usuarioService.login(new LoginRequest("fantasma", "huerto123")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getEstado());
        assertEquals(UsuarioService.CREDENCIALES_INVALIDAS, error.getMessage());
    }

    // Prueba: cuenta desactivada → 403 Forbidden
    @Test
    void loginConCuentaDesactivada_lanza403() {
        when(usuarioRepository.findByNombreUsuario("docente")).thenReturn(Optional.of(docente(false)));

        ApiException error = assertThrows(ApiException.class,
                () -> usuarioService.login(new LoginRequest("docente", "huerto123")));

        assertEquals(HttpStatus.FORBIDDEN, error.getEstado());
    }

    // Prueba: buscar un id que no existe → 404 Not Found
    @Test
    void buscarIdInexistente_lanza404() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ApiException error = assertThrows(ApiException.class, () -> usuarioService.buscarPorId(99L));

        assertEquals(HttpStatus.NOT_FOUND, error.getEstado());
    }

    // Prueba: nombre de usuario repetido → 409 y NO se llama a save()
    @Test
    void crearUsuarioRepetido_lanza409YNoGuarda() {
        when(usuarioRepository.existsByNombreUsuario("docente")).thenReturn(true);

        UsuarioRequest request = new UsuarioRequest("docente", "Otra persona", "clave123", Rol.ESTUDIANTE, "6° Básico A");
        ApiException error = assertThrows(ApiException.class, () -> usuarioService.crear(request));

        assertEquals(HttpStatus.CONFLICT, error.getEstado());
        // verify(..., never()) comprueba que el método NUNCA se llamó
        verify(usuarioRepository, never()).save(any());
    }

    // Prueba: al crear, se guarda el HASH (nunca la contraseña) y el usuario en minúsculas
    @Test
    void crearUsuario_guardaElHashYNoLaPassword() {
        when(usuarioRepository.existsByNombreUsuario("estudiante4")).thenReturn(false);
        // save devuelve el mismo objeto que recibe
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        usuarioService.crear(new UsuarioRequest("Estudiante4", "Ana Pérez", "clave123", Rol.ESTUDIANTE, "6° Básico A"));

        // ArgumentCaptor "atrapa" el objeto que se envió a save() para revisarlo
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario guardado = captor.getValue();

        assertEquals("estudiante4", guardado.getNombreUsuario()); // se guarda en minúsculas
        assertNotEquals("clave123", guardado.getPasswordHash());
        assertEquals(Seguridad.hashPassword("clave123"), guardado.getPasswordHash());
    }
}
