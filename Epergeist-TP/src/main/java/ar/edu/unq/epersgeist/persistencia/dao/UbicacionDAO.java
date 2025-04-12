package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dto.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.UbicacionJPADTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionDAO extends JpaRepository<UbicacionJPADTO, Long> {
    @Query(
            "FROM Espiritu e WHERE e.ubicacion.id = :ubicacionId AND e.fechaEliminado IS NULL"
    )
    List<EspirituJPADTO> espiritusEn(@Param("ubicacionId") long ubicacionId);



    @Query(
            "SELECT m FROM Medium m LEFT JOIN m.espiritus e " +
                    "WHERE m.ubicacion.id = :ubicacionId AND e.fechaEliminado IS NULL " +
                    "GROUP BY m.id " +
                    "HAVING COUNT(e) = 0"
    )
    List<MediumJPADTO> mediumsSinEspiritusEn(@Param("ubicacionId") Long ubicacionId);



    // --------------------------------------------------------------

    @Query("SELECT u " +
            "FROM Ubicacion u " +
            "JOIN Espiritu e ON u.id = e.ubicacion.id " +
            "WHERE u.tipo = 'SANTUARIO' " +
            "AND e.fechaEliminado IS NULL " +
            "GROUP BY u.id " +
            "ORDER BY SUM(CASE WHEN e.tipo = 'DEMONIO' THEN 1 ELSE 0 END) - " +
            "SUM(CASE WHEN e.tipo = 'ANGEL' THEN 1 ELSE 0 END) DESC " +
            "LIMIT 1"
    )
    UbicacionJPADTO obtenerSantuarioMasCorrupto();


    @Query(
            "SELECT m " +
                    "FROM Medium m LEFT JOIN m.espiritus e " +
                    "WHERE m.ubicacion.id = :ubicacionId AND e.tipo = 'DEMONIO' AND e.fechaEliminado IS NULL " +
                    "GROUP BY m.id " +
                    "ORDER BY COUNT(e), m.mana DESC " +  //en caso de empate de cantidad de demonios, se elige el medium con mayor mana
                    "LIMIT 1"

    )
    MediumJPADTO mediumConMayorCantidadDemoniosEn(@Param("ubicacionId") Long ubicacionId);


    @Query(
            "SELECT COUNT(e) " +
                    "FROM Espiritu e " +
                    "WHERE e.ubicacion.id = :ubicacionId AND e.tipo = 'DEMONIO' AND e.fechaEliminado IS NULL"
    )
    int cantidadTotalDemoniosEn(@Param("ubicacionId") Long ubicacionId);



    @Query(
            "SELECT COUNT(e) " +
                    "FROM Espiritu e " +
                    "WHERE e.ubicacion.id = :ubicacionId AND e.tipo = 'DEMONIO' AND e.mediumConectado IS NULL AND e.fechaEliminado IS NULL"
    )
    int cantidadDemoniosLibresEn(@Param("ubicacionId") Long ubicacionId);

}
