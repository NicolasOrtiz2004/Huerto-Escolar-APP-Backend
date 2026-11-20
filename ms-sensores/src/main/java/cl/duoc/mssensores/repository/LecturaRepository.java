package cl.duoc.mssensores.repository;

import cl.duoc.mssensores.model.LecturaSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ==========================================================================
// ARCHIVO: LecturaRepository.java (capa de acceso a datos de la tabla lecturas_sensor)
// Interfaz JPA: Spring Data crea las consultas SQL a partir del nombre de cada método.
// ==========================================================================
// Spring genera la implementación de esta interfaz
// Repositorio de lecturas (clave primaria Long)
@Repository
public interface LecturaRepository extends JpaRepository<LecturaSensor, Long> {

    /** Las más nuevas primero (igual que la consulta del SensorDao de la app). */
    List<LecturaSensor> findAllByOrderByFechaDesc();

    // SELECT * FROM lecturas_sensor WHERE sector_id = ? ORDER BY fecha DESC
    List<LecturaSensor> findBySectorIdOrderByFechaDesc(Long sectorId);
}
