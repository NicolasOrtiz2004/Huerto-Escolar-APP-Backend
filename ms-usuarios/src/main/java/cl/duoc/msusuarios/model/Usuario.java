package cl.duoc.msusuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * ENTIDAD USUARIO → tabla "usuarios" en MySQL (BD huerto_usuarios)
 * Es la misma tabla que tiene la app en Room, pero aquí vive la versión
 * "oficial" compartida por todas las tablets.
 *
 * Nunca se guarda la contraseña: solo su hash SHA-256 (ver util/Seguridad).
 */
// @Entity = esta clase es una TABLA. Cada objeto Usuario = una FILA.
// @Table: nombre exacto de la tabla en MySQL
// Hibernate crea la tabla sola (ddl-auto=update) a partir de estos campos
@Entity
@Table(name = "usuarios")
public class Usuario {

    // @Id = clave primaria (PRIMARY KEY)
    // Número único de cada usuario (1, 2, 3...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT de MySQL
    private Long id;

    // nullable = false → NOT NULL · unique = true → no se puede repetir · length → VARCHAR(30)
    // Con esto se inicia sesión (ej: "docente")
    @Column(nullable = false, unique = true, length = 30)
    private String nombreUsuario;

    // Nombre ficticio para mostrar (ej: "Prof. Marta Quiroga")
    @Column(nullable = false, length = 60)
    private String nombre;

    // Hash SHA-256 de la contraseña (64 caracteres hexadecimales)
    @Column(nullable = false, length = 64)
    private String passwordHash;

    // ADMINISTRADOR o ESTUDIANTE
    @Enumerated(EnumType.STRING) // guarda "ADMINISTRADOR" en vez de 0/1
    @Column(nullable = false, length = 20)
    private Rol rol;

    // Taller o curso del usuario (ej: "6° Básico A")
    @Column(nullable = false, length = 60)
    private String taller;

    // false = cuenta desactivada (no puede iniciar sesión)
    @Column(nullable = false)
    private boolean activo = true;

    /** JPA necesita un constructor vacío para crear el objeto al leer de la BD. */
    // protected: solo JPA lo usa; nosotros usamos el constructor de abajo
    protected Usuario() {
    }

    // Constructor para crear un usuario NUEVO (sin id: lo asigna MySQL al guardar)
    public Usuario(String nombreUsuario, String nombre, String passwordHash, Rol rol, String taller, boolean activo) {
        // "this.x = x" copia el parámetro al campo del objeto
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.taller = taller;
        this.activo = activo;
    }

    // ---------- Getters y setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getTaller() { return taller; }
    public void setTaller(String taller) { this.taller = taller; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
