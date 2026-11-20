package cl.duoc.mshuerto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/*
 * ENTIDAD CULTIVO → tabla "cultivos"
 *
 * @ManyToOne: MUCHOS cultivos pertenecen a UN sector. En MySQL queda la
 * columna sector_id como CLAVE FORÁNEA hacia sectores(id) (mismo modelo 3FN
 * que en Room).
 *
 * La foto del cultivo NO se guarda aquí: se queda en el teléfono que la tomó
 * (privacidad del caso y para no subir imágenes por la red).
 */
// Tabla "cultivos" de MySQL
// Nombre de la tabla
@Entity
@Table(name = "cultivos")
public class Cultivo {

    // Clave primaria
    // AUTO_INCREMENT de MySQL
    // Id del cultivo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre común de la planta
    @Column(nullable = false, length = 40)
    private String nombre;

    // Descripción corta
    @Column(nullable = false, length = 200)
    private String descripcion;

    // @JoinColumn = nombre de la columna de la clave foránea en la tabla cultivos
    // El sector al que pertenece (objeto completo; en la BD solo se guarda su id)
    @ManyToOne(optional = false) // obligatorio: todo cultivo está en un sector
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    // LocalDate = solo fecha, sin hora (DATE en MySQL)
    @Column(nullable = false)
    private LocalDate fechaSiembra;

    // Fecha de trasplante (puede ser null)
    private LocalDate fechaTrasplante; // opcional

    // Fecha estimada de cosecha (nunca antes que la siembra)
    @Column(nullable = false)
    private LocalDate fechaCosechaEstimada;

    // Cada cuántos días se riega (1 a 30)
    @Column(nullable = false)
    private int frecuenciaRiegoDias;

    // Enum guardado como texto
    // Estado del cultivo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private EstadoCultivo estado;

    // Notas opcionales (por defecto texto vacío)
    @Column(nullable = false, length = 300)
    private String observaciones = "";

    // Constructor vacío obligatorio para JPA
    protected Cultivo() {
    }

    // Constructor para crear un cultivo nuevo (sin id)
    public Cultivo(String nombre, String descripcion, Sector sector, LocalDate fechaSiembra, LocalDate fechaTrasplante,
                   LocalDate fechaCosechaEstimada, int frecuenciaRiegoDias, EstadoCultivo estado, String observaciones) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sector = sector;
        this.fechaSiembra = fechaSiembra;
        this.fechaTrasplante = fechaTrasplante;
        this.fechaCosechaEstimada = fechaCosechaEstimada;
        this.frecuenciaRiegoDias = frecuenciaRiegoDias;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // get = leer un campo · set = cambiarlo
    // ---------- Getters y setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Sector getSector() { return sector; }
    public void setSector(Sector sector) { this.sector = sector; }

    public LocalDate getFechaSiembra() { return fechaSiembra; }
    public void setFechaSiembra(LocalDate fechaSiembra) { this.fechaSiembra = fechaSiembra; }

    public LocalDate getFechaTrasplante() { return fechaTrasplante; }
    public void setFechaTrasplante(LocalDate fechaTrasplante) { this.fechaTrasplante = fechaTrasplante; }

    public LocalDate getFechaCosechaEstimada() { return fechaCosechaEstimada; }
    public void setFechaCosechaEstimada(LocalDate fechaCosechaEstimada) { this.fechaCosechaEstimada = fechaCosechaEstimada; }

    public int getFrecuenciaRiegoDias() { return frecuenciaRiegoDias; }
    public void setFrecuenciaRiegoDias(int frecuenciaRiegoDias) { this.frecuenciaRiegoDias = frecuenciaRiegoDias; }

    public EstadoCultivo getEstado() { return estado; }
    public void setEstado(EstadoCultivo estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
