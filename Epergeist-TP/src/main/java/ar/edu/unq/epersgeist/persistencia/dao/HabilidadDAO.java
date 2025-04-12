package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.persistencia.dto.HabilidadJPADTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface HabilidadDAO extends JpaRepository<HabilidadJPADTO, Long> {

    @Query("select h.nombre from HabilidadJPADTO h where h.id = :habilidadId")
    String recuperarNombrePorID(@Param("habilidadId") Long habilidadId);

    @Query("select h from HabilidadJPADTO h where h.nombre = :nombreHabilidad")
    HabilidadJPADTO recuperarPorNombre(@Param("nombreHabilidad") String nombreHabilidad);

    @Query("select h from HabilidadJPADTO h where h.nombre in :habilidadesPosiblesAUnPaso")
    Set<HabilidadJPADTO> recuperarPorNombres(Set<String> habilidadesPosiblesAUnPaso);
}
