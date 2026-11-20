package cl.duoc.mssensores.exception;

import cl.duoc.mssensores.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/*
 * MANEJADOR GLOBAL DE ERRORES
 * ===========================
 * @RestControllerAdvice "escucha" las excepciones de TODOS los controladores
 * y las convierte en una respuesta JSON ordenada con el código HTTP correcto.
 * Así los controladores quedan limpios (sin try/catch).
 *
 *   400 Bad Request  → datos inválidos
 *   404 Not Found    → ruta inexistente
 *   500              → error inesperado
 */
@RestControllerAdvice
public class ManejadorErrores {

    // Logger para escribir errores inesperados en la consola
    private static final Logger log = LoggerFactory.getLogger(ManejadorErrores.class);

    /** Errores de negocio lanzados por los servicios. */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> manejarApi(ApiException e) {
        return ResponseEntity.status(e.getEstado()).body(new ErrorResponse(e.getMessage()));
    }

    /** Falló alguna validación (@NotBlank, @Size...): devolvemos el PRIMER mensaje. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException e) {
        // Se toma el mensaje del primer campo que falló la validación
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");
        return ResponseEntity.badRequest().body(new ErrorResponse(mensaje));
    }

    /** El JSON viene mal escrito o con un valor que no existe (ej: humedad "mucha"). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("El cuerpo de la petición no es válido. Revisa el JSON"));
    }

    /** Un parámetro de la URL tiene un tipo incorrecto (ej: /api/lecturas/sector/abc). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipo(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("El parámetro '" + e.getName() + "' no tiene un valor válido"));
    }

    /** La ruta no existe. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRutaInexistente(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("La ruta solicitada no existe"));
    }

    /** Se usó un método HTTP que la ruta no acepta (ej: DELETE donde solo hay GET). */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> manejarMetodo(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse("Método HTTP no permitido en esta ruta"));
    }

    /** Se violó una restricción de la BD (ej: UNIQUE). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarIntegridad(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("El dato ya existe o viola una restricción de la base de datos"));
    }

    /** Cualquier otro error: no mostramos detalles internos al cliente. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception e) {
        // Detalle completo solo en la consola; al cliente va un mensaje genérico
        log.error("Error inesperado", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno del servidor"));
    }
}
