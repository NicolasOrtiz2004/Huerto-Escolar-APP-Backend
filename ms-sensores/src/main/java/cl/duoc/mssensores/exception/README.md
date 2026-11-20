# 📁 exception/

Manejo centralizado de errores: todas las respuestas de error tienen la forma { "mensaje": "..." }.

| Archivo / carpeta | Qué hace |
|---|---|
| `ApiException.java` | Excepción de negocio que lleva su código HTTP (400, 401, 404, 409...). |
| `ManejadorErrores.java` | @RestControllerAdvice: atrapa las excepciones de todos los controladores y responde el JSON de error con el código correcto. |
| `README.md` | Este archivo. |
