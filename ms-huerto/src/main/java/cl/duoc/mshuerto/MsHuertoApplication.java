package cl.duoc.mshuerto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * MICROSERVICIO DEL HUERTO (puerto 8082)
 * ======================================
 * Responsabilidad: los SECTORES y CULTIVOS del huerto. Es el "catálogo"
 * compartido: si la docente crea un sector en una tablet, las demás lo
 * reciben al sincronizar.
 *
 * Se comunica con ms-usuarios (client/UsuarioClient) para comprobar que el
 * responsable de un sector sea un docente o tallerista activo.
 */
// Activa la autoconfiguración de Spring Boot y busca los componentes de este paquete
@SpringBootApplication
public class MsHuertoApplication {

    // Punto de partida: levanta el servidor en el puerto 8082
    public static void main(String[] args) {
        // Arranca Spring (Tomcat + conexión a MySQL + ejecuta DatosIniciales)
        SpringApplication.run(MsHuertoApplication.class, args);
    }
}
