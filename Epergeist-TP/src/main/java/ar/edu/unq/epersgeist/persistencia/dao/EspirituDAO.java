package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dto.EspirituJPADTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<EspirituJPADTO, Long> {
    @Query(
            "FROM Espiritu e WHERE e.tipo = 'DEMONIO' AND e.fechaEliminado IS NULL ORDER BY e.energia DESC"
    )
    List<EspirituJPADTO> recuperarEspiritusDemoniacos();

    @Query("SELECT e FROM Espiritu e LEFT JOIN FETCH e.habilidad WHERE e.id = :espirituId AND e.fechaEliminado IS NULL")
    EspirituJPADTO recuperarEspiritu(@Param("espirituId") Long espirituId);

    @Query(
            "FROM Espiritu e WHERE e.tipo = 'DEMONIO' AND e.fechaEliminado IS NULL"
    )
    Page<EspirituJPADTO> recuperarEspiritusDemoniacos(Pageable pageable);

    @Query(
            "FROM Espiritu e WHERE e.fechaEliminado IS NULL"
    )
    List<EspirituJPADTO> recuperarTodos();

}