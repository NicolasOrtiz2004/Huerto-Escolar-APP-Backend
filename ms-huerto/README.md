# 📁 ms-huerto (puerto 8082, BD huerto_sectores)

Sectores y cultivos. Llama a ms-usuarios para validar al responsable de un sector. Capas: Controller → Service → Repository (JPA) → MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `pom.xml` | Proyecto Maven: Spring Boot 3.5.7, Java 17 y dependencias (web, JPA, validación, MySQL, Swagger, test). |
| `src/main/resources/application.properties` | Configuración: puerto 8082, conexión a MySQL (huerto_sectores, se crea sola), JPA y Swagger y URL de ms-usuarios. |
| `src/main/java/` | Código Java (ver README de cada paquete). |
| `src/test/java/` | Pruebas unitarias (JUnit 5 + Mockito + MockMvc). |
| `README.md` | Este archivo. |
