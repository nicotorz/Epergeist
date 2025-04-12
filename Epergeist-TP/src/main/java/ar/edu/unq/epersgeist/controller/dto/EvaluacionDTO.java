package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Evaluacion;

import java.util.Set;
import java.util.stream.Collectors;

public record EvaluacionDTO (Evaluacion evaluacion) {

        public static Set<Evaluacion> aModelo(Set<EvaluacionDTO> evaluacion){
            return evaluacion.stream().map(EvaluacionDTO::aModelo).collect(Collectors.toSet());
        }

        public Evaluacion aModelo(){
            return this.evaluacion;
        }
}
