package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Medium;

public class MediumSinSuficienteManaException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Medium medium;


    public MediumSinSuficienteManaException(Medium medium) {  this.medium = medium;  }

    @Override
    public String getMessage() {
        return "El medium " + medium.getId()+ " no tiene suficiente mana";
    }
}

