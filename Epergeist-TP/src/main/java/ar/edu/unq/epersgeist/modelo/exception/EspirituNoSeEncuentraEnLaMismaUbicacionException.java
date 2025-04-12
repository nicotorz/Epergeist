package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

public class EspirituNoSeEncuentraEnLaMismaUbicacionException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Espiritu espiritu;
    private final Medium medium;

    public EspirituNoSeEncuentraEnLaMismaUbicacionException(Medium medium, Espiritu espiritu) {
        this.espiritu = espiritu;
        this.medium = medium;
    }

    @Override
    public String getMessage() {
        return "El espiritu " + espiritu.getId() + " no esta en la misma ubicacion del medium " + medium.getId() ;
    }
}

