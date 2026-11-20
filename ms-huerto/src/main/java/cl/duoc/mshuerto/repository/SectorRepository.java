package cl.duoc.mshuerto.repository;

import cl.duoc.mshuerto.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ==========================================================================
// ARCHIVO: SectorRepository.java (capa de acceso a datos de la tabla sectores)
// Interfaz JPA: Spring Data crea las consultas SQL a partir del nombre de cada método.
// ==========================================================================
// Spring genera la implementación de esta interfaz
// JpaRepository ya trae findAll, findById, save, delete, count...
@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    // SELECT * FROM sectores ORDER BY codigo ASC
    List<Sector> findAllByOrderByCodigoAsc();

    /** ¿Ya existe un sector con este código? (al CREAR) */
    boolean existsByCodigoIgnoreCase(String codigo);

    /** ¿Otro sector distinto al que edito ya usa este código? (al EDITAR) */
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
}
