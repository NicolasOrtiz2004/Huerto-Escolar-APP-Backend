package cl.duoc.mshuerto.model;

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
 * ENTIDAD SECTOR → tabla "sectores" (BD huerto_sectores)
 *
 * responsableId guarda el id de un usuario de OTRO microservicio (ms-usuarios).
 * No hay clave foránea porque cada microservicio tiene su PROPIA base de
 * datos ("database per service"). Por eso la validación se hace llamando a
 * ms-usuarios por HTTP (ver client/UsuarioClient).
 */
// @Entity = esta clase es una tabla de MySQL
// Nombre de la tabla
@Entity
@Table(name = "sectores")
public class Sector {

    // Clave primaria
    // IDENTITY = el id lo genera MySQL con AUTO_INCREMENT
    // Id del sector
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NOT NULL + UNIQUE: no pueden existir dos sectores "S-01"
    // Código del sector (ej: S-01)
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    // Nombre (ej: Bancal Norte)
    @Column(nullable = false, length = 40)
    private String nombre;

    // Descripción corta
    @Column(nullable = false, length = 200)
    private String descripcion;

    // Dónde está dentro del huerto
    @Column(nullable = false, length = 60)
    private String ubicacion;

    // Guarda el enum como texto ("EN_USO") y no como número
    // Estado actual del sector
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSector estado;

    private Long responsableId; // opcional

    // Fecha y hora de creación (LocalDateTime → DATETIME en MySQL)
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    // Fecha y hora del último cambio
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    // Constructor vacío obligatorio para JPA
    protected Sector() {
    }

    // Constructor para crear un sector nuevo: las fechas se ponen solas con "ahora"
    public Sector(String codigo, String nombre, String descripcion, String ubicacion, EstadoSector estado, Long responsableId) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.responsableId = responsableId;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = this.fechaCreacion;
    }

    // get = leer un campo · set = cambiarlo. JPA y los servicios los usan
    // ---------- Getters y setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public EstadoSector getEstado() { return estado; }
    public void setEstado(EstadoSector estado) { this.estado = estado; }

    public Long getResponsableId() { return responsableId; }
    public void setResponsableId(Long responsableId) { this.responsableId = responsableId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
