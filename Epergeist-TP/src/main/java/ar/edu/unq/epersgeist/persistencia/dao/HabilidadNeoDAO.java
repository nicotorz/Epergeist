package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.persistencia.dto.HabilidadNeo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface HabilidadNeoDAO extends Neo4jRepository<HabilidadNeo, Long> {

    @Query("MATCH (h: HabilidadNeo {nombre: $nombre}) " +
            "OPTIONAL MATCH (h)-[c:CondicionDeMutacion]-(h2)" +
            "RETURN h, collect(c), collect(h2)")
    Optional<HabilidadNeo> findByNombre(@Param("nombre") String nombre);


    @Query("MATCH(h: HabilidadNeo) DETACH DELETE h")
    void detachDelete();


    @Query("MATCH (h: HabilidadNeo {nombre: $nombreHabilidadOrigen}) " +
            "MATCH (h2: HabilidadNeo {nombre: $nombreHabilidadDestino}) " +
            "MERGE (h)-[c:CondicionDeMutacion {cantidadNecesaria: $cantidadNecesaria, tipoDeCondicion: $tipoDeCondicion}]->(h2)")
    void descubrirHabilidad(@Param("nombreHabilidadOrigen") String habilidadOrigen,
                            @Param("nombreHabilidadDestino") String habilidadDestino,
                            @Param("cantidadNecesaria") Integer cantidadNecesaria,
                            @Param("tipoDeCondicion") Evaluacion evaluacion);



    @Query("MATCH (h:HabilidadNeo {nombre: $nombreHabilidad})" +
            "MATCH (h)-[:CondicionDeMutacion]-(h2)" +
            "RETURN h2")
    Set<HabilidadNeo> habilidadesConectadas(String nombreHabilidad);


    @Query("MATCH path = (h:HabilidadNeo {nombre: $nombreHabilidadOrigen})-[:CondicionDeMutacion*]->(mut:HabilidadNeo) " +
            "WHERE ALL(rel IN relationships(path) WHERE " +
            "  (rel.cantidadNecesaria <= CASE rel.tipoDeCondicion " +
            "   WHEN 'ENERGIA' THEN $energia " +
            "   WHEN 'NIVELDECONEXION' THEN $nivelDeConexion " +
            "   WHEN 'EXORICISMOSEVITADOS' THEN $exorcismosEvitados " +
            "   WHEN 'EXORICISMOSRESUELTOS' THEN $exorcismosResueltos " +
            "  END)) " +
            "WITH path, length(path) AS pathLength " +
            "ORDER BY pathLength DESC " +
            "LIMIT 1 " +
            "RETURN [n IN nodes(path) WHERE n.nombre <> $nombreHabilidadOrigen] AS habilidades")
    List<HabilidadNeo> caminoMasMutable(@Param("nombreHabilidadOrigen") String nombreHabilidadOrigen, @Param("energia") int energia,
                                        @Param("nivelDeConexion") int nivelDeConexion,
                                        @Param("exorcismosEvitados") int exorcismosEvitados,
                                        @Param("exorcismosResueltos") int exorcismosResueltos);



    @Query("MATCH path = (h:HabilidadNeo {nombre: $nombreHabilidadOrigen})-[:CondicionDeMutacion*]->(mut:HabilidadNeo) " +
            "WHERE ALL(rel IN relationships(path) WHERE " +
            "  rel.cantidadNecesaria <= CASE rel.tipoDeCondicion " +
            "   WHEN 'ENERGIA' THEN $energia " +
            "   WHEN 'NIVELDECONEXION' THEN $nivelDeConexion " +
            "   WHEN 'EXORICISMOSEVITADOS' THEN $exorcismosEvitados " +
            "   WHEN 'EXORICISMOSRESUELTOS' THEN $exorcismosResueltos " +
            "  END) " +
            "WITH path, length(path) AS pathLength " +
            "ORDER BY pathLength ASC " +
            "LIMIT 1 " +
            "RETURN [n IN nodes(path) WHERE n.nombre <> $nombreHabilidadOrigen] AS habilidades")
    List<HabilidadNeo> caminoMenosMutable(@Param("nombreHabilidadOrigen") String nombreHabilidadOrigen,
                                          @Param("energia") int energia,
                                          @Param("nivelDeConexion") int nivelDeConexion,
                                          @Param("exorcismosEvitados") int exorcismosEvitados,
                                          @Param("exorcismosResueltos") int exorcismosResueltos);



    @Query("MATCH (inicio:HabilidadNeo {nombre: $nombreHabilidadOrigen}), (fin:HabilidadNeo {nombre: $nombreHabilidadDestino})" +
            "MATCH caminoMasRapido = shortestPath((inicio)-[:CondicionDeMutacion*]->(fin)) " +
            "WHERE all(condicion IN relationships(caminoMasRapido) " +
            "WHERE condicion.tipoDeCondicion IN $condiciones) " +
            "RETURN caminoMasRapido "
    )
    List<HabilidadNeo> caminoMasRentable(@Param("nombreHabilidadOrigen") String nombreHabilidadOrigen,
                                         @Param("nombreHabilidadDestino") String nombreHabilidadDestino,
                                         @Param("condiciones") Set<Evaluacion> evaluaciones);



    @Query("MATCH (inicio:HabilidadNeo {nombre: $nombreHabilidadOrigen}), (fin:HabilidadNeo {nombre: $nombreHabilidadDestino})" +
            "RETURN EXISTS((inicio)-[:CondicionDeMutacion*]->(fin))")
    boolean estanConectados(@Param("nombreHabilidadOrigen") String nombreHabilidadOrigen,
                            @Param("nombreHabilidadDestino") String nombreHabilidadDestino);



    @Query( "MATCH (h:HabilidadNeo)-[mutacion:CondicionDeMutacion]->(hNueva:HabilidadNeo) " +
            "WHERE mutacion.cantidadNecesaria <= CASE mutacion.tipoDeCondicion " +
            "WHEN 'ENERGIA' THEN $energia " +
            "WHEN 'NIVELDECONEXION' THEN $nivelDeConexion " +
            "WHEN 'EXORCISMOSEVITADOS' THEN $exorcismosEvitados " +
            "WHEN 'EXORCISMOSRESUELTOS' THEN $exorcismosResueltos " +
            "ELSE false END " +
            "AND NOT hNueva.nombre IN $HabilidadesAdquiridas " +
            "RETURN DISTINCT hNueva")
    Set<HabilidadNeo> habilidadesPosibles(@Param("energia") int energia,
                                          @Param("nivelDeConexion") int nivelDeConexion,
                                          @Param("exorcismosEvitados") int exorcismosEvitados,
                                          @Param("exorcismosResueltos") int exorcismosResueltos,
                                          @Param("HabilidadesAdquiridas") Set<String> habilidadNombre);



    @Query( "MATCH (h:HabilidadNeo) " +
            "WHERE h.nombre IN $nombresOrigen " +
            "MATCH (h)-[mutacion:CondicionDeMutacion]->(hNueva:HabilidadNeo) " +
            "WHERE mutacion.cantidadNecesaria <= CASE mutacion.tipoDeCondicion " +
            "WHEN 'ENERGIA' THEN $energia " +
            "WHEN 'NIVELDECONEXION' THEN $nivelDeConexion " +
            "WHEN 'EXORCISMOSEVITADOS' THEN $exorcismosEvitados " +
            "WHEN 'EXORCISMOSRESUELTOS' THEN $exorcismosResueltos " +
            "ELSE false END " +
            "AND NOT hNueva.nombre IN $nombresOrigen " +
            "RETURN DISTINCT hNueva.nombre")
    Set<String> habilidadesPosiblesAUnPaso( @Param("nombresOrigen") Set<String> nombresOrigen,
                                                  @Param("energia") int energia,
                                                  @Param("nivelDeConexion") int nivelDeConexion,
                                                  @Param("exorcismosEvitados") int exorcismosEvitados,
                                                  @Param("exorcismosResueltos") int exorcismosResueltos);


}