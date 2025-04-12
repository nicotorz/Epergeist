package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Evaluacion;

public record SimpleCondicionDTO(Long id, Integer cantidadNecesaria, Evaluacion evaluacion) {

    public static SimpleCondicionDTO desdeModelo(Condicion condicion) {
        return new SimpleCondicionDTO(
                condicion.getId(),
                condicion.getCantidadNecesaria(),
                condicion.getEvaluacion()
        );
    }

    public Condicion aModelo() {
        Condicion condicion = new Condicion(this.cantidadNecesaria, this.evaluacion);
        condicion.setId(this.id);
        return condicion;
    }

}
