package cl.duoc.msusuarios.repository;

import cl.duoc.msusuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * REPOSITORIO JPA
 * Igual que el DAO de Room en la app: define QUÉ consultas existen.
 * Spring Data genera el SQL a partir del NOMBRE del método:
 *   findByNombreUsuario → SELECT * FROM usuarios WHERE nombre_usuario = ?
 * JpaRepository ya trae findAll, findById, save, deleteById, count...
 */
// @Repository: Spring crea una implementación de esta interfaz y la "inyecta" donde se necesite
// JpaRepository<Usuario, Long> = repositorio de la entidad Usuario cuya clave primaria es Long
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Optional = "puede venir vacío" (si el usuario no existe). Evita los NullPointerException.
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // SELECT COUNT(*) > 0 ... → true si ya existe ese nombre de usuario (para no repetirlo)
    boolean existsByNombreUsuario(String nombreUsuario);

    // SELECT * FROM usuarios ORDER BY nombre ASC → lista ordenada alfabéticamente
    List<Usuario> findAllByOrderByNombreAsc();
}
