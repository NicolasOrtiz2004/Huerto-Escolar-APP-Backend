package cl.duoc.mshuerto.exception;

import org.springframework.http.HttpStatus;

/**
 * Error de negocio con su código HTTP.
 * Ej: new ApiException(HttpStatus.CONFLICT, "Ya existe un sector con el código S-01")
 * El ManejadorErrores la transforma en la respuesta JSON { "mensaje": ... }.
 */
// RuntimeException = no obliga a poner try/catch en cada método
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
