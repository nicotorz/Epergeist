package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Habilidad;

import java.util.Set;

public record  HabilidadDTO(Long id, String nombre, Set<Condicion> condicion) {

    public static HabilidadDTO desdeModelo(Habilidad habilidad){
        return new HabilidadDTO(
                habilidad.getId(),
                habilidad.getNombre(),
                habilidad.getCondicion()
        );
    }


    public Habilidad aModelo(){
        Habilidad habilidad = new Habilidad(this.nombre);
        habilidad.setId(this.id);
        habilidad.setCondicion(this.condicion);
        return habilidad;
    }
}
