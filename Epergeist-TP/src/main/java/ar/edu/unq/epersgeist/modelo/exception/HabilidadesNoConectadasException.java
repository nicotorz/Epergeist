package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Habilidad;

import java.io.Serial;

public class HabilidadesNoConectadasException extends RuntimeException {

    public HabilidadesNoConectadasException(String habilidadOrigen ,String habilidadDestino) {
        this.habilidadOrigen = habilidadOrigen;
        this.habilidadDestino =habilidadDestino;
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final String habilidadOrigen;
    private final String habilidadDestino;


    @Override
    public String getMessage() {
        return "La habilidad" + habilidadOrigen+ "no esta conenctada con la habilidad" + habilidadDestino;
    }

}
