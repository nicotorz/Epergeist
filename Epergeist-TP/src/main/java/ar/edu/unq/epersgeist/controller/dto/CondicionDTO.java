package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;

public record CondicionDTO(Integer cantidadNecesaria, Evaluacion evaluacion) {

    public Condicion aModelo(){
        return new Condicion(this.cantidadNecesaria, this.evaluacion);
    }
}
