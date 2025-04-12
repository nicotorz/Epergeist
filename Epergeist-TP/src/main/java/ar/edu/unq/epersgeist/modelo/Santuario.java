package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.SoloSePuedenInvocarAngelesEnUnSatuario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
@NoArgsConstructor
public class Santuario extends Ubicacion{

    public Santuario(String nombre, int energia){
        super(nombre,energia);
    }

    @Override
    public void validarInvocacion(Espiritu espiritu){
            if(espiritu.estaEndemoniado()) {
                throw new SoloSePuedenInvocarAngelesEnUnSatuario(espiritu);
            }
        }

    @Override
    public void descansarEnUbicacion(Medium medium) {
        medium.recuperarManaPorDescanso((int) (this.getEnergia()*1.5));
        List<EspirituAngelical> espiritus = medium.espiritusAngelicales();

        espiritus.stream().forEach(espiritu -> {
            espiritu.recuperarEnergia(this.getEnergia());
        });
    }

    @Override
    public void actualizarEnergia(Espiritu espiritu) {
        if (espiritu.estaEndemoniado()) {
            espiritu.perderEnergia(10);
        }
    }

    @Override
    public boolean esSantuario() {
        return true;
    }

    @Override
    public UbicacionTipo getTipo() {
        return UbicacionTipo.SANTUARIO;
    }
}
