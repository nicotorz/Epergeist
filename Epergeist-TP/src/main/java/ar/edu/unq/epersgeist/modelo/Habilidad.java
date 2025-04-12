package ar.edu.unq.epersgeist.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Habilidad {

    private Long id;
    private String nombre;
    private Set<Condicion> condicion;


    public Habilidad(String nombre) {
        this.nombre = nombre;
        this.condicion = new HashSet<>();
    }
}
