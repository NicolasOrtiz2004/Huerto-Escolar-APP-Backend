package cl.duoc.msusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * MICROSERVICIO DE USUARIOS (puerto 8081)
 * =======================================
 * Responsabilidad ÚNICA: las cuentas del huerto y el inicio de sesión.
 * La app Android le pregunta aquí si el usuario y la contraseña son correctos.
 *
 * Capas (igual idea que el MVVM de la app):
 *   Controller (recibe HTTP) → Service (reglas) → Repository (JPA) → MySQL
 */
// @SpringBootApplication junta 3 anotaciones:
// - @Configuration: esta clase puede definir configuración
// - @EnableAutoConfiguration: Spring configura solo Tomcat, JPA, Jackson, etc. según las dependencias del pom
// - @ComponentScan: busca @RestController, @Service, @Repository, @Component en este paquete y sus subpaquetes
@SpringBootApplication
public class MsUsuariosApplication {

    // main = punto de partida del programa (como en cualquier app Java).
    public static void main(String[] args) {
        // Arranca Spring Boot: levanta el servidor Tomcat en el puerto 8081 y conecta a MySQL
        SpringApplication.run(MsUsuariosApplication.class, args);
    }
}
