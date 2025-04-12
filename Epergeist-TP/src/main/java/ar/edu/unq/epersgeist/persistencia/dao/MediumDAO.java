package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dto.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.MediumJPADTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediumDAO extends JpaRepository<MediumJPADTO, Long> {
    @Query(
            "select e from Medium m join m.espiritus e where m.id = :mediumId and e.fechaEliminado = null "
    )
    List<EspirituJPADTO> espiritusDe(@Param("mediumId") Long mediumId);
}
