# 🗺️ Mapa del proyecto — huerto-escolar-backend

Todas las carpetas y archivos de los 3 microservicios con lo que hace cada uno. Cada paquete tiene además su propio `README.md`.

> Flujo de una petición: **Controller** (recibe HTTP y valida con @Valid) → **Service** (reglas) → **Repository** (JPA) → **MySQL**. Si algo falla, **ManejadorErrores** responde `{ "mensaje": ... }`.


## `huerto-escolar-backend/` — huerto-escolar-backend (raíz)

Carpeta que se abre en IntelliJ. Junta los 3 microservicios, cada uno es un proyecto Maven independiente.

| Archivo / carpeta | Qué hace |
|---|---|
| `pom.xml` | POM agregador: lista los 3 módulos para importarlos juntos y correr `mvn test` en todos. |
| `MAPA_DEL_PROYECTO.md` | Este mapa. |
| `.gitignore` | Ignora carpetas target/ (compilación) y archivos del IDE. |
| `.run/Backend completo.run.xml` | Configuración COMPUESTA de IntelliJ: arranca los 3 microservicios con un solo ▶. |
| `.run/ms-usuarios.run.xml` | Configuración de IntelliJ que ejecuta MsUsuariosApplication. |
| `.run/ms-huerto.run.xml` | Configuración de IntelliJ que ejecuta MsHuertoApplication. |
| `.run/ms-sensores.run.xml` | Configuración de IntelliJ que ejecuta MsSensoresApplication. |


## `huerto-escolar-backend/ms-usuarios/` — ms-usuarios (puerto 8081, BD huerto_usuarios)

Cuentas e inicio de sesión (contraseñas como hash SHA-256). Capas: Controller → Service → Repository (JPA) → MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `pom.xml` | Proyecto Maven: Spring Boot 3.5.7, Java 17 y dependencias (web, JPA, validación, MySQL, Swagger, test). |
| `src/main/resources/application.properties` | Configuración: puerto 8081, conexión a MySQL (huerto_usuarios, se crea sola), JPA y Swagger. |
| `src/main/java/` | Código Java (ver README de cada paquete). |
| `src/test/java/` | Pruebas unitarias (JUnit 5 + Mockito + MockMvc). |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/` — Paquete cl.duoc.msusuarios

Paquete raíz de ms-usuarios. Spring busca componentes (@RestController, @Service...) desde aquí hacia abajo.

| Archivo / carpeta | Qué hace |
|---|---|
| `MsUsuariosApplication.java` | Clase con main(): arranca Spring Boot en el puerto 8081. @SpringBootApplication activa la autoconfiguración. |
| `model/` | Entidades JPA (tablas) y enums. |
| `repository/` | Interfaces JpaRepository (consultas). |
| `dto/` | Records con la forma del JSON de entrada y salida. |
| `service/` | Reglas de negocio. |
| `controller/` | Endpoints REST. |
| `exception/` | Errores y su respuesta JSON. |
| `config/` | Carga de datos de prueba al arrancar. |
| `util/` | Utilidades (hash de contraseñas). |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/exception/` — exception/

Manejo centralizado de errores: todas las respuestas de error tienen la forma { "mensaje": "..." }.

| Archivo / carpeta | Qué hace |
|---|---|
| `ApiException.java` | Excepción de negocio que lleva su código HTTP (400, 401, 404, 409...). |
| `ManejadorErrores.java` | @RestControllerAdvice: atrapa las excepciones de todos los controladores y responde el JSON de error con el código correcto. |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/config/` — config/

Configuración que se ejecuta al arrancar.

| Archivo / carpeta | Qué hace |
|---|---|
| `DatosIniciales.java` | CommandLineRunner: si la tabla está vacía carga los datos de prueba (mismo orden que la app para que los id coincidan). |


## `huerto-escolar-backend/ms-huerto/` — ms-huerto (puerto 8082, BD huerto_sectores)

Sectores y cultivos. Llama a ms-usuarios para validar al responsable de un sector. Capas: Controller → Service → Repository (JPA) → MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `pom.xml` | Proyecto Maven: Spring Boot 3.5.7, Java 17 y dependencias (web, JPA, validación, MySQL, Swagger, test). |
| `src/main/resources/application.properties` | Configuración: puerto 8082, conexión a MySQL (huerto_sectores, se crea sola), JPA y Swagger y URL de ms-usuarios. |
| `src/main/java/` | Código Java (ver README de cada paquete). |
| `src/test/java/` | Pruebas unitarias (JUnit 5 + Mockito + MockMvc). |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/` — Paquete cl.duoc.mshuerto

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


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/exception/` — exception/

Manejo centralizado de errores: todas las respuestas de error tienen la forma { "mensaje": "..." }.

| Archivo / carpeta | Qué hace |
|---|---|
| `ApiException.java` | Excepción de negocio que lleva su código HTTP (400, 401, 404, 409...). |
| `ManejadorErrores.java` | @RestControllerAdvice: atrapa las excepciones de todos los controladores y responde el JSON de error con el código correcto. |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/config/` — config/

Configuración que se ejecuta al arrancar.

| Archivo / carpeta | Qué hace |
|---|---|
| `DatosIniciales.java` | CommandLineRunner: si la tabla está vacía carga los datos de prueba (mismo orden que la app para que los id coincidan). |


## `huerto-escolar-backend/ms-sensores/` — ms-sensores (puerto 8083, BD huerto_sensores)

Lecturas SIMULADAS de humedad y temperatura (origen "Simulación académica"). Capas: Controller → Service → Repository (JPA) → MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `pom.xml` | Proyecto Maven: Spring Boot 3.5.7, Java 17 y dependencias (web, JPA, validación, MySQL, Swagger, test). |
| `src/main/resources/application.properties` | Configuración: puerto 8083, conexión a MySQL (huerto_sensores, se crea sola), JPA y Swagger. |
| `src/main/java/` | Código Java (ver README de cada paquete). |
| `src/test/java/` | Pruebas unitarias (JUnit 5 + Mockito + MockMvc). |


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/` — Paquete cl.duoc.mssensores

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


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/exception/` — exception/

Manejo centralizado de errores: todas las respuestas de error tienen la forma { "mensaje": "..." }.

| Archivo / carpeta | Qué hace |
|---|---|
| `ApiException.java` | Excepción de negocio que lleva su código HTTP (400, 401, 404, 409...). |
| `ManejadorErrores.java` | @RestControllerAdvice: atrapa las excepciones de todos los controladores y responde el JSON de error con el código correcto. |


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/config/` — config/

Configuración que se ejecuta al arrancar.

| Archivo / carpeta | Qué hace |
|---|---|
| `DatosIniciales.java` | CommandLineRunner: si la tabla está vacía carga los datos de prueba (mismo orden que la app para que los id coincidan). |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/model/` — model/

Entidades JPA = tablas de MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `Usuario.java` | Tabla usuarios: id, nombreUsuario (único), nombre, passwordHash, rol, taller, activo. |
| `Rol.java` | Enum ADMINISTRADOR / ESTUDIANTE. |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/repository/` — repository/

Acceso a datos con Spring Data JPA (el SQL se genera por el nombre del método).

| Archivo / carpeta | Qué hace |
|---|---|
| `UsuarioRepository.java` | findByNombreUsuario, existsByNombreUsuario, findAllByOrderByNombreAsc + los heredados (save, findById...). |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/dto/` — dto/

Records con la forma del JSON. Las anotaciones (@NotBlank, @Size...) validan la entrada.

| Archivo / carpeta | Qué hace |
|---|---|
| `LoginRequest.java` | Entrada del login: nombreUsuario y password. |
| `UsuarioRequest.java` | Entrada para crear una cuenta. |
| `UsuarioResponse.java` | Salida de un usuario (SIN el hash). |
| `ErrorResponse.java` | Salida de error: { "mensaje" }. |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/service/` — service/

Reglas de negocio.

| Archivo / carpeta | Qué hace |
|---|---|
| `UsuarioService.java` | login (existe → clave correcta → cuenta activa), listar, buscarPorId (404) y crear (409 si se repite). |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/controller/` — controller/

Endpoints REST (reciben HTTP y responden JSON).

| Archivo / carpeta | Qué hace |
|---|---|
| `AuthController.java` | POST /api/auth/login. |
| `UsuarioController.java` | GET /api/usuarios, GET /api/usuarios/{id}, POST /api/usuarios. |


## `huerto-escolar-backend/ms-usuarios/src/main/java/cl/duoc/msusuarios/util/` — util/

Utilidades.

| Archivo / carpeta | Qué hace |
|---|---|
| `Seguridad.java` | Hash SHA-256 de contraseñas (igual que la app) y comparación. |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/model/` — model/

Entidades JPA = tablas de MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `Sector.java` | Tabla sectores: código único, nombre, descripción, ubicación, estado, responsableId y fechas. |
| `Cultivo.java` | Tabla cultivos: @ManyToOne con Sector (FK sector_id), fechas LocalDate, frecuencia de riego, estado. |
| `EstadoSector.java` | Enum de estados del sector. |
| `EstadoCultivo.java` | Enum de estados del cultivo. |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/repository/` — repository/

Acceso a datos con Spring Data JPA.

| Archivo / carpeta | Qué hace |
|---|---|
| `SectorRepository.java` | Orden por código y comprobación de código repetido (al crear y al editar). |
| `CultivoRepository.java` | Orden por nombre y filtro por sector (findBySector_Id...). |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/dto/` — dto/

Records con la forma del JSON.

| Archivo / carpeta | Qué hace |
|---|---|
| `SectorRequest.java` | Entrada para crear/editar sector (con validaciones, ej: @Pattern del código S-01). |
| `SectorResponse.java` | Salida de un sector. |
| `CultivoRequest.java` | Entrada para crear/editar cultivo (fechas "yyyy-MM-dd", frecuencia 1-30). |
| `CultivoResponse.java` | Salida de un cultivo (con sectorId). |
| `UsuarioRemoto.java` | JSON que responde ms-usuarios (lo lee UsuarioClient). |
| `ErrorResponse.java` | Salida de error. |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/service/` — service/

Reglas de negocio.

| Archivo / carpeta | Qué hace |
|---|---|
| `SectorService.java` | Código único y en mayúsculas; verifica al responsable en ms-usuarios. |
| `CultivoService.java` | Sector existente y no fuera de uso; cosecha ≥ siembra; trasplante entre ambas. |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/controller/` — controller/

Endpoints REST.

| Archivo / carpeta | Qué hace |
|---|---|
| `SectorController.java` | GET/POST /api/sectores, GET/PUT /api/sectores/{id}, GET /api/sectores/{id}/cultivos. |
| `CultivoController.java` | GET/POST /api/cultivos (filtro ?sectorId=), GET/PUT/DELETE /api/cultivos/{id}. |


## `huerto-escolar-backend/ms-huerto/src/main/java/cl/duoc/mshuerto/client/` — client/

Comunicación ENTRE microservicios.

| Archivo / carpeta | Qué hace |
|---|---|
| `UsuarioClient.java` | RestClient que llama a GET ms-usuarios /api/usuarios/{id} para comprobar que el responsable sea un admin activo (404 → 400, caído → 503). |


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/model/` — model/

Entidades JPA.

| Archivo / carpeta | Qué hace |
|---|---|
| `LecturaSensor.java` | Tabla lecturas_sensor: sectorId, fecha, humedad de suelo, temperatura, humedad ambiente, origen y estado. |
| `EstadoLectura.java` | Enum NORMAL / BAJO / ALTO / PENDIENTE_REVISION. |


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/repository/` — repository/

Acceso a datos con Spring Data JPA.

| Archivo / carpeta | Qué hace |
|---|---|
| `LecturaRepository.java` | Lecturas ordenadas por fecha (todas o de un sector). |


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/dto/` — dto/

Records con la forma del JSON.

| Archivo / carpeta | Qué hace |
|---|---|
| `LecturaResponse.java` | Salida de una lectura. |
| `LecturaRequest.java` | Entrada de una lectura manual (rangos validados). |
| `SimulacionRequest.java` | Entrada para simular: lista de ids de sectores (no vacía). |
| `ErrorResponse.java` | Salida de error. |


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/service/` — service/

Reglas de negocio.

| Archivo / carpeta | Qué hace |
|---|---|
| `LecturaService.java` | Simular (una lectura por sector, sin repetir), registrar manual, clasificar (<30 Bajo, >80 Alto) y redondear. |


## `huerto-escolar-backend/ms-sensores/src/main/java/cl/duoc/mssensores/controller/` — controller/

Endpoints REST.

| Archivo / carpeta | Qué hace |
|---|---|
| `LecturaController.java` | GET /api/lecturas, GET /api/lecturas/sector/{id}, POST /api/lecturas/simular, POST /api/lecturas. |


## `huerto-escolar-backend/ms-usuarios/src/test/java/cl/duoc/msusuarios/service/` — src/test/.../service/

Pruebas UNITARIAS del servicio: el repositorio es un @Mock (no usa MySQL). Ejecutar: clic derecho → Run.

| Archivo / carpeta | Qué hace |
|---|---|
| `UsuarioServiceTest.java` | 7 pruebas del servicio con Mockito (login, 401, 403, 404, 409, hash). |


## `huerto-escolar-backend/ms-usuarios/src/test/java/cl/duoc/msusuarios/controller/` — src/test/.../controller/

Pruebas de la capa HTTP con @WebMvcTest + MockMvc (sin servidor ni BD). Ejecutar: clic derecho → Run.

| Archivo / carpeta | Qué hace |
|---|---|
| `AuthControllerTest.java` | 3 pruebas HTTP con MockMvc (200, 400, 401). |


## `huerto-escolar-backend/ms-huerto/src/test/java/cl/duoc/mshuerto/service/` — src/test/.../service/

Pruebas UNITARIAS del servicio: el repositorio es un @Mock (no usa MySQL). Ejecutar: clic derecho → Run.

| Archivo / carpeta | Qué hace |
|---|---|
| `SectorServiceTest.java` | 6 pruebas (código repetido, mayúsculas, llamada a ms-usuarios, 404). |
| `CultivoServiceTest.java` | 4 pruebas de fechas y sector fuera de uso. |


## `huerto-escolar-backend/ms-huerto/src/test/java/cl/duoc/mshuerto/controller/` — src/test/.../controller/

Pruebas de la capa HTTP con @WebMvcTest + MockMvc (sin servidor ni BD). Ejecutar: clic derecho → Run.

| Archivo / carpeta | Qué hace |
|---|---|
| `SectorControllerTest.java` | 3 pruebas HTTP (lista, código inválido, estado inexistente). |


## `huerto-escolar-backend/ms-sensores/src/test/java/cl/duoc/mssensores/service/` — src/test/.../service/

Pruebas UNITARIAS del servicio: el repositorio es un @Mock (no usa MySQL). Ejecutar: clic derecho → Run.

| Archivo / carpeta | Qué hace |
|---|---|
| `LecturaServiceTest.java` | 4 pruebas (umbrales, rangos aleatorios, simulación, lectura manual). |


## `huerto-escolar-backend/ms-sensores/src/test/java/cl/duoc/mssensores/controller/` — src/test/.../controller/

Pruebas de la capa HTTP con @WebMvcTest + MockMvc (sin servidor ni BD). Ejecutar: clic derecho → Run.

| Archivo / carpeta | Qué hace |
|---|---|
| `LecturaControllerTest.java` | 4 pruebas HTTP (400 lista vacía, 201, 400 rango, 200). |
