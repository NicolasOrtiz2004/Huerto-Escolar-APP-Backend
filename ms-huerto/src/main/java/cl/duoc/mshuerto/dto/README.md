# 📁 dto/

Records con la forma del JSON.

| Archivo / carpeta | Qué hace |
|---|---|
| `SectorRequest.java` | Entrada para crear/editar sector (con validaciones, ej: @Pattern del código S-01). |
| `SectorResponse.java` | Salida de un sector. |
| `CultivoRequest.java` | Entrada para crear/editar cultivo (fechas "yyyy-MM-dd", frecuencia 1-30). |
| `CultivoResponse.java` | Salida de un cultivo (con sectorId). |
| `UsuarioRemoto.java` | JSON que responde ms-usuarios (lo lee UsuarioClient). |
| `ErrorResponse.java` | Salida de error. |
| `README.md` | Este archivo. |
