package cl.duoc.msusuarios.exception;

import org.springframework.http.HttpStatus;

/**
 * Error de negocio con su código HTTP.
 * Ej: new ApiException(HttpStatus.CONFLICT, "Ya existe el usuario docente")
 * El ManejadorErrores la transforma en la respuesta JSON { "mensaje": ... }.
 */
// extends RuntimeException = excepción "no verificada": no obliga a poner try/catch en cada método
public class ApiException extends RuntimeException {

    // Código HTTP que se responderá (ej: HttpStatus.CONFLICT = 409)
    private final HttpStatus estado;

    // super(mensaje) guarda el texto en la excepción (se lee con getMessage())
    public ApiException(HttpStatus estado, String mensaje) {
        super(mensaje);
        this.estado = estado;
    }

    // El ManejadorErrores lo usa para saber qué código HTTP responder
    public HttpStatus getEstado() {
        return estado;
    }
}
