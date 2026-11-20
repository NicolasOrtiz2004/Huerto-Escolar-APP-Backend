package cl.duoc.mssensores.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/*
 * ENTIDAD LECTURA → tabla "lecturas_sensor" (BD huerto_sensores)
 * sectorId es el id de un sector de ms-huerto (otra BD, por eso no hay FK).
 */
// Tabla "lecturas_sensor" en MySQL
// Nombre de la tabla
@Entity
@Table(name = "lecturas_sensor")
public class LecturaSensor {

    /** Texto exigido por el caso para que nadie confunda el dato con una medición real. */
    // static final = constante compartida por toda la clase
    public static final String ORIGEN_SIMULADO = "Simulación académica";

    // Clave primaria
    // AUTO_INCREMENT de MySQL
    // Id de la lectura
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Id del sector (de ms-huerto)
    @Column(nullable = false)
    private Long sectorId;

    // Fecha y hora de la lectura
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private double humedadSuelo;    // %

    @Column(nullable = false)
    private double temperatura;     // °C

    @Column(nullable = false)
    private double humedadAmbiente; // %

    // De dónde viene el dato (simulación o ingreso manual)
    @Column(nullable = false, length = 40)
    private String origen = ORIGEN_SIMULADO;

    // Enum guardado como texto
    // NORMAL / BAJO / ALTO (se calcula, no se recibe)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private EstadoLectura estado;

    // Constructor vacío obligatorio para JPA
    protected LecturaSensor() {
    }

    // Constructor para crear una lectura nueva
    public LecturaSensor(Long sectorId, LocalDateTime fecha, double humedadSuelo, double temperatura,
                         double humedadAmbiente, String origen, EstadoLectura estado) {
        this.sectorId = sectorId;
        this.fecha = fecha;
        this.humedadSuelo = humedadSuelo;
        this.temperatura = temperatura;
        this.humedadAmbiente = humedadAmbiente;
        this.origen = origen;
        this.estado = estado;
    }

    // get = leer un campo · set = cambiarlo
    // ---------- Getters y setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSectorId() { return sectorId; }
    public void setSectorId(Long sectorId) { this.sectorId = sectorId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public double getHumedadSuelo() { return humedadSuelo; }
    public void setHumedadSuelo(double humedadSuelo) { this.humedadSuelo = humedadSuelo; }

    public double getTemperatura() { return temperatura; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }

    public double getHumedadAmbiente() { return humedadAmbiente; }
    public void setHumedadAmbiente(double humedadAmbiente) { this.humedadAmbiente = humedadAmbiente; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public EstadoLectura getEstado() { return estado; }
    public void setEstado(EstadoLectura estado) { this.estado = estado; }
}
