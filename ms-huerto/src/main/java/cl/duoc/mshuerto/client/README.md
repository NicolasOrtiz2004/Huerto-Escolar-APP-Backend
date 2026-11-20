# 📁 client/

Comunicación ENTRE microservicios.

| Archivo / carpeta | Qué hace |
|---|---|
| `UsuarioClient.java` | RestClient que llama a GET ms-usuarios /api/usuarios/{id} para comprobar que el responsable sea un admin activo (404 → 400, caído → 503). |
| `README.md` | Este archivo. |
