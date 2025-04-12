package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.dto.HabilidadNeo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

@Getter @Setter
@RelationshipProperties
public class Condicion {

    @RelationshipId
    private Long id;


    private Integer cantidadNecesaria;

    private Evaluacion evaluacion;

    @TargetNode
    private HabilidadNeo habilidad;

    public Condicion(Integer cantidadNecesaria, Evaluacion evaluacion) {
        this.cantidadNecesaria = cantidadNecesaria;
        this.evaluacion = evaluacion;
    }



}
