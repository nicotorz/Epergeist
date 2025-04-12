package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;

import java.io.Serial;

public class SoloSePuedenInvocarDemoniosEnUnCementerio extends RuntimeException {

    public SoloSePuedenInvocarDemoniosEnUnCementerio(Espiritu espiritu) {
        this.espiritu = espiritu;
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final Espiritu espiritu;




    @Override
    public String getMessage() {
        return "El tipo " + espiritu.getTipo() + " no se puede invocar en cementerios";
    }
}



