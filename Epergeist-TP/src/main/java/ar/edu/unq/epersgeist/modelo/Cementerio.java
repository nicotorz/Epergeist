package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.EspirituOcupadoException;
import ar.edu.unq.epersgeist.modelo.exception.SoloSePuedenInvocarDemoniosEnUnCementerio;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class Cementerio extends Ubicacion{

    public Cementerio(String nombre, int energia){
        super(nombre,energia);
    }

    @Override
    public void validarInvocacion(Espiritu espiritu){
        if(!espiritu.estaEndemoniado()) {
            throw new SoloSePuedenInvocarDemoniosEnUnCementerio(espiritu);
        }
    }

    @Override
    public void descansarEnUbicacion(Medium medium) {
        medium.recuperarManaPorDescanso(this.getEnergia()/2);
        List<EspirituDemoniaco> espiritus = medium.espiritusDemonicos();

        espiritus.stream().forEach(espiritu -> {
                espiritu.recuperarEnergia(this.getEnergia());
            });
    }

    @Override
    public void actualizarEnergia(Espiritu espiritu) {
        if (!espiritu.estaEndemoniado()) {
            espiritu.perderEnergia(5);
        }
    }

    @Override
    public boolean esSantuario() {
        return false;
    }

    @Override
    public UbicacionTipo getTipo() {
        return UbicacionTipo.CEMENTERIO;
    }

}
