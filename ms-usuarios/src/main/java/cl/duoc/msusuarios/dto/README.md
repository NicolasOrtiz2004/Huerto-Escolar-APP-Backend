# 📁 dto/

Records con la forma del JSON. Las anotaciones (@NotBlank, @Size...) validan la entrada.

| Archivo / carpeta | Qué hace |
|---|---|
| `LoginRequest.java` | Entrada del login: nombreUsuario y password. |
| `UsuarioRequest.java` | Entrada para crear una cuenta. |
| `UsuarioResponse.java` | Salida de un usuario (SIN el hash). |
| `ErrorResponse.java` | Salida de error: { "mensaje" }. |
| `README.md` | Este archivo. |
