# 📁 ms-sensores (puerto 8083, BD huerto_sensores)

Lecturas SIMULADAS de humedad y temperatura (origen "Simulación académica"). Capas: Controller → Service → Repository (JPA) → MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `pom.xml` | Proyecto Maven: Spring Boot 3.5.7, Java 17 y dependencias (web, JPA, validación, MySQL, Swagger, test). |
| `src/main/resources/application.properties` | Configuración: puerto 8083, conexión a MySQL (huerto_sensores, se crea sola), JPA y Swagger. |
| `src/main/java/` | Código Java (ver README de cada paquete). |
| `src/test/java/` | Pruebas unitarias (JUnit 5 + Mockito + MockMvc). |
| `README.md` | Este archivo. |
