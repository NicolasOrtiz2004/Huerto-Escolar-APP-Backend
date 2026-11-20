package cl.duoc.mssensores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * MICROSERVICIO DE SENSORES (puerto 8083)
 * =======================================
 * Genera y guarda lecturas SIMULADAS de humedad de suelo, temperatura y
 * humedad ambiente por sector. Todas quedan marcadas con el origen
 * "Simulación académica" (requisito del caso: no hay Arduino ni ESP32).
 *
 * Idea a futuro: un sensor real (ESP32) podría enviar sus mediciones a
 * POST /api/lecturas sin cambiar nada en la app.
 */
// Activa la autoconfiguración de Spring Boot y busca los componentes de este paquete
@SpringBootApplication
public class MsSensoresApplication {

    // Punto de partida: levanta el servidor en el puerto 8083
    public static void main(String[] args) {
        // Arranca Spring (Tomcat + MySQL + DatosIniciales)
        SpringApplication.run(MsSensoresApplication.class, args);
    }
}
