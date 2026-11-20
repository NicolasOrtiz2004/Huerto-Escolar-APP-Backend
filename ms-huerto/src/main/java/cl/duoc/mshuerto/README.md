# 📁 Paquete cl.duoc.mshuerto

Paquete raíz de ms-huerto. Spring busca componentes (@RestController, @Service...) desde aquí hacia abajo.

| Archivo / carpeta | Qué hace |
|---|---|
| `MsHuertoApplication.java` | Clase con main(): arranca Spring Boot en el puerto 8082. @SpringBootApplication activa la autoconfiguración. |
| `model/` | Entidades JPA (tablas) y enums. |
| `repository/` | Interfaces JpaRepository (consultas). |
| `dto/` | Records con la forma del JSON de entrada y salida. |
| `service/` | Reglas de negocio. |
| `controller/` | Endpoints REST. |
| `exception/` | Errores y su respuesta JSON. |
| `config/` | Carga de datos de prueba al arrancar. |
| `client/` | Cliente HTTP hacia ms-usuarios (comunicación entre microservicios). |
| `README.md` | Este archivo. |
