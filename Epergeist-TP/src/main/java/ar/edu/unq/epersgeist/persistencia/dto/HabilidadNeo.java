package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Getter@Setter
@Node
public class HabilidadNeo {
    @Id @GeneratedValue
    private Long id;

    @Property(name = "nombre")
    @Column(unique = true)
    private String nombre;

    @Relationship(type = "CondicionDeMutacion", direction = Relationship.Direction.OUTGOING)
    private Set<Condicion> condiciones = new HashSet<>();


    public static HabilidadNeo desdeModelo(Habilidad habilidad) {
        HabilidadNeo dto = new HabilidadNeo();
        dto.id  = habilidad.getId();
        dto.nombre = habilidad.getNombre();
        return dto;
    }

    public Habilidad aModelo() {
        Habilidad habilidad = new Habilidad(this.nombre);
        habilidad.setCondicion(this.condiciones);
        habilidad.setId(this.id);
        return habilidad;
    }
}
