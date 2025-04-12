package ar.edu.unq.epersgeist.modelo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString

public abstract class Ubicacion {
    private Long id;

    private String nombre;

    @Min(0) @Max(100) // Limites de eneriga
    private int energia;

    public Ubicacion(String nombre, int energia) {
        this.nombre = nombre;
        this.energia = energia;
    }

    public abstract void validarInvocacion(Espiritu espiritu);

    public abstract void descansarEnUbicacion(Medium medium);

    public abstract void actualizarEnergia(Espiritu espiritu);

    public abstract boolean esSantuario();


    public abstract UbicacionTipo getTipo();
}
