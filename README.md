# 🖥️ Huerto Escolar — Backend (microservicios Spring Boot)

Backend de la app Android **Huerto Escolar** (DSY1105 · Duoc UC). Son **3 microservicios independientes**, cada uno con su propia base de datos MySQL.

| Microservicio | Puerto | Base de datos | Responsabilidad |
|---|---|---|---|
| `ms-usuarios` | 8081 | `huerto_usuarios` | Cuentas e inicio de sesión |
| `ms-huerto` | 8082 | `huerto_sectores` | Sectores y cultivos (consulta a ms-usuarios para validar responsables) |
| `ms-sensores` | 8083 | `huerto_sensores` | Lecturas **simuladas** de humedad y temperatura |

🗺️ **Qué hace cada carpeta y archivo:** [`MAPA_DEL_PROYECTO.md`](MAPA_DEL_PROYECTO.md) (cada paquete tiene además su `README.md`).

**Stack:** Java 17 · Spring Boot 3.5.7 · Spring Web · Spring Data JPA · Bean Validation · MySQL 8 · Swagger UI (springdoc) · JUnit 5 + Mockito + MockMvc.

## ▶️ Cómo ejecutar

1. **Encender MySQL** (Laragon o XAMPP) en el puerto **3306**, usuario `root` **sin contraseña**.
   *Tip:* en las preferencias de Laragon puedes activar que inicie sus servicios automáticamente al abrirlo.
   No hay que crear nada: las bases de datos y tablas se crean solas al arrancar.
   *Si tu MySQL tiene contraseña:* edita `spring.datasource.password=` en el `application.properties` de cada microservicio (o define la variable de entorno `DB_PASSWORD`).
2. Abrir esta carpeta (`huerto-escolar-backend`) en **IntelliJ IDEA** → se importan los 3 proyectos Maven (pom agregador).
3. **Un solo clic:** arriba a la derecha, en la lista de configuraciones, elige **"Backend completo"** y presiona ▶.
   Se abren 3 pestañas en la ventana *Run* (una por microservicio). Para apagar todo: botón ⏹ (Stop All).
   > Las configuraciones vienen en la carpeta `.run/`. Si no aparecen, espera a que termine de importar Maven o usa `File → Reload All from Disk`.
   *Alternativa manual:* ▶ junto al `main` de `MsUsuariosApplication`, `MsHuertoApplication` y `MsSensoresApplication`.
4. Verificar en el navegador:
   - http://localhost:8081/swagger-ui.html
   - http://localhost:8082/swagger-ui.html
   - http://localhost:8083/swagger-ui.html

Al primer arranque cada microservicio carga datos ficticios (los mismos de la app, en el mismo orden para que los id coincidan). Contraseña de todas las cuentas: `huerto123`.

> Desde la terminal también se puede: `mvn spring-boot:run` dentro de cada carpeta `ms-...`.

## 🧪 Pruebas unitarias (31)

```bash
mvn test            # en esta carpeta: ejecuta las pruebas de los 3 microservicios
```
O en IntelliJ: clic derecho en `src/test/java` → **Run 'All Tests'**. **No necesitan MySQL encendido**: los repositorios se reemplazan por *mocks* de Mockito.

| Microservicio | Pruebas | Qué verifican |
|---|---|---|
| ms-usuarios | `UsuarioServiceTest` (7), `AuthControllerTest` (3) | Login correcto/incorrecto/desactivado, usuario repetido, se guarda el hash y no la contraseña, respuestas HTTP |
| ms-huerto | `SectorServiceTest` (6), `CultivoServiceTest` (4), `SectorControllerTest` (3) | Código repetido, llamada a ms-usuarios, reglas de fechas, sector fuera de uso, validaciones 400 |
| ms-sensores | `LecturaServiceTest` (4), `LecturaControllerTest` (4) | Umbrales de humedad, rangos realistas, simulación sin repetir sectores, validaciones 400 |

## 📡 Endpoints

Todos los errores responden: `{ "mensaje": "texto del error" }`

### ms-usuarios (8081)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Inicio de sesión → 200 / 401 / 403 |
| GET | `/api/usuarios` | Lista de cuentas |
| GET | `/api/usuarios/{id}` | Una cuenta (la usa ms-huerto) |
| POST | `/api/usuarios` | Crear cuenta → 201 / 409 |

### ms-huerto (8082)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/sectores` | Lista de sectores |
| GET | `/api/sectores/{id}` | Un sector |
| GET | `/api/sectores/{id}/cultivos` | Cultivos de un sector |
| POST | `/api/sectores` | Crear sector → 201 / 400 / 409 / 503 |
| PUT | `/api/sectores/{id}` | Editar sector |
| GET | `/api/cultivos?sectorId=` | Lista de cultivos (filtro opcional) |
| GET | `/api/cultivos/{id}` | Un cultivo |
| POST | `/api/cultivos` | Crear cultivo → 201 / 400 / 404 / 409 |
| PUT | `/api/cultivos/{id}` | Editar cultivo |
| DELETE | `/api/cultivos/{id}` | Eliminar cultivo → 204 |

### ms-sensores (8083)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/lecturas` | Todas las lecturas (más nuevas primero) |
| GET | `/api/lecturas/sector/{sectorId}` | Lecturas de un sector |
| POST | `/api/lecturas/simular` | Genera una lectura aleatoria por sector → 201 |
| POST | `/api/lecturas` | Registra una lectura manual (futuro sensor real) → 201 |

## 🧾 Ejemplos para Postman

**Login**
```http
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{ "nombreUsuario": "docente", "password": "huerto123" }
```

**Crear sector** (el responsable 1 = docente se valida en ms-usuarios)
```http
POST http://localhost:8082/api/sectores
Content-Type: application/json

{ "codigo": "S-05", "nombre": "Bancal Sur", "descripcion": "Bancal de flores nativas",
  "ubicacion": "Junto al gimnasio", "estado": "EN_USO", "responsableId": 1 }
```

**Crear cultivo**
```http
POST http://localhost:8082/api/cultivos
Content-Type: application/json

{ "nombre": "Lechuga", "descripcion": "Lechuga escarola", "sectorId": 1,
  "fechaSiembra": "2026-11-20", "fechaTrasplante": null, "fechaCosechaEstimada": "2026-12-30",
  "frecuenciaRiegoDias": 2, "estado": "SEMBRADO", "observaciones": "" }
```

**Simular lecturas**
```http
POST http://localhost:8083/api/lecturas/simular
Content-Type: application/json

{ "sectorIds": [1, 2, 3] }
```

## 🗂️ Estructura de cada microservicio
```
ms-xxx/src/main/java/cl/duoc/msxxx/
├── MsXxxApplication.java   → punto de entrada (main)
├── model/        → @Entity (tablas) y enums
├── repository/   → JpaRepository (consultas)
├── dto/          → records con la forma del JSON (Request / Response)
├── service/      → reglas de negocio
├── controller/   → @RestController (endpoints)
├── exception/    → ApiException + ManejadorErrores (@RestControllerAdvice)
├── config/       → DatosIniciales (carga datos de prueba)
└── client/       → (solo ms-huerto) UsuarioClient: llamada HTTP a ms-usuarios
```

## ⚠️ Problemas comunes
| Problema | Solución |
|---|---|
| `Communications link failure` al arrancar | MySQL no está encendido (abre Laragon/XAMPP) |
| `Access denied for user 'root'` | Tu MySQL tiene contraseña: ponla en `spring.datasource.password` |
| `Port 8081 was already in use` | Ya hay una instancia corriendo: detenla en IntelliJ (⏹ Stop All) |
| No aparece "Backend completo" | Maven aún está importando, o abriste una carpeta `ms-...` en vez de `huerto-escolar-backend` |
| Crear sector responde 503 | `ms-usuarios` está apagado (ms-huerto lo necesita para validar al responsable) |
| La app dice "Modo sin conexión" | Revisa que los 3 microservicios estén corriendo y usa el emulador (IP 10.0.2.2) |
