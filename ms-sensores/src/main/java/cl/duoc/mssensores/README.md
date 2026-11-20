# 📁 Paquete cl.duoc.mssensores

Paquete raíz de ms-sensores. Spring busca componentes (@RestController, @Service...) desde aquí hacia abajo.

| Archivo / carpeta | Qué hace |
|---|---|
| `MsSensoresApplication.java` | Clase con main(): arranca Spring Boot en el puerto 8083. @SpringBootApplication activa la autoconfiguración. |
| `model/` | Entidades JPA (tablas) y enums. |
| `repository/` | Interfaces JpaRepository (consultas). |
| `dto/` | Records con la forma del JSON de entrada y salida. |
| `service/` | Reglas de negocio. |
| `controller/` | Endpoints REST. |
| `exception/` | Errores y su respuesta JSON. |
| `config/` | Carga de datos de prueba al arrancar. |
| `README.md` | Este archivo. |
