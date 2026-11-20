package cl.duoc.mssensores.exception;

import org.springframework.http.HttpStatus;

/**
 * Error de negocio con su código HTTP.
 * Ej: new ApiException(HttpStatus.BAD_REQUEST, "Debes indicar al menos un sector")
 * El ManejadorErrores la transforma en la respuesta JSON { "mensaje": ... }.
 */
// RuntimeException = no obliga a poner try/catch
public class ApiException extends RuntimeException {

    // Código HTTP que se responderá
    private final HttpStatus estado;

    // super(mensaje) guarda el texto del error
    public ApiException(HttpStatus estado, String mensaje) {
        super(mensaje);
        this.estado = estado;
    }

    // Lo lee el ManejadorErrores
    public HttpStatus getEstado() {
        return estado;
    }
}
