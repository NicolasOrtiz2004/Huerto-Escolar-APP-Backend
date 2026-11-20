package cl.duoc.mshuerto.repository;

import cl.duoc.mshuerto.model.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ==========================================================================
// ARCHIVO: CultivoRepository.java (capa de acceso a datos de la tabla cultivos)
// Interfaz JPA: Spring Data crea las consultas SQL a partir del nombre de cada método.
// ==========================================================================
// Spring genera la implementación de esta interfaz
// Repositorio de la entidad Cultivo (clave primaria Long)
@Repository
public interface CultivoRepository extends JpaRepository<Cultivo, Long> {

    // SELECT * FROM cultivos ORDER BY nombre ASC
    List<Cultivo> findAllByOrderByNombreAsc();

    /** "Sector_Id" = navega la relación: WHERE sector_id = ? */
    List<Cultivo> findBySector_IdOrderByNombreAsc(Long sectorId);
}
