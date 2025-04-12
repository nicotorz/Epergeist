package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;

public class EspirituOcupadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Espiritu espiritu;


    public EspirituOcupadoException (Espiritu espiritu) {  this.espiritu = espiritu;  }

    @Override
    public String getMessage() {
        return "El espiritu " + espiritu.getId() + " no esta libre";
    }
}