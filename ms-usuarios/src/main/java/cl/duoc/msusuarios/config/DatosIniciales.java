package cl.duoc.msusuarios.config;

import cl.duoc.msusuarios.model.Rol;
import cl.duoc.msusuarios.model.Usuario;
import cl.duoc.msusuarios.repository.UsuarioRepository;
import cl.duoc.msusuarios.util.Seguridad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * DATOS INICIALES FICTICIOS (seed)
 * ================================
 * CommandLineRunner se ejecuta UNA vez al arrancar el microservicio.
 * Si la tabla está vacía, crea las mismas 6 cuentas que trae la app
 * (DatosIniciales.kt). Se insertan EN EL MISMO ORDEN para que los id
 * coincidan (1 = docente, 2 = tallerista, 3..5 = estudiantes, 6 = inactivo).
 * Contraseña de todas: huerto123
 */
// @Component: Spring crea este objeto al arrancar; como implementa CommandLineRunner, ejecuta run()
@Component
public class DatosIniciales implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatosIniciales.class);
    // Contraseña de prueba de todas las cuentas (igual que en la app)
    private static final String PASSWORD_DEMO = "huerto123";

    // Repositorio para insertar las cuentas
    private final UsuarioRepository usuarioRepository;

    public DatosIniciales(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // @Override = este método viene de la interfaz CommandLineRunner
    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return; // ya se cargaron antes
        }
        // Se calcula el hash una sola vez y se usa en las 6 cuentas
        String hash = Seguridad.hashPassword(PASSWORD_DEMO);
        // saveAll inserta la lista EN ORDEN → los id quedan 1, 2, 3, 4, 5, 6
        usuarioRepository.saveAll(List.of(
                new Usuario("docente", "Prof. Marta Quiroga", hash, Rol.ADMINISTRADOR, "Taller de Huerto", true),
                new Usuario("tallerista", "Julián Ferrada", hash, Rol.ADMINISTRADOR, "Taller de Huerto", true),
                new Usuario("estudiante1", "Tomás Alarcón", hash, Rol.ESTUDIANTE, "6° Básico A", true),
                new Usuario("estudiante2", "Sofía Lillo", hash, Rol.ESTUDIANTE, "6° Básico A", true),
                new Usuario("estudiante3", "Benjamín Urra", hash, Rol.ESTUDIANTE, "7° Básico B", true),
                new Usuario("inactivo", "Cuenta de prueba inactiva", hash, Rol.ESTUDIANTE, "8° Básico", false)
        ));
        // Mensaje en la consola para confirmar que se cargaron los datos
        log.info("ms-usuarios: se cargaron 6 cuentas de prueba (contraseña: {})", PASSWORD_DEMO);
    }
}
