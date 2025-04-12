package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.SoloSePuedenInvocarAngelesEnUnSatuario;
import ar.edu.unq.epersgeist.modelo.exception.SoloSePuedenInvocarDemoniosEnUnCementerio;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class EspirituDemoniaco extends  Espiritu {

    public EspirituDemoniaco( @NonNull String nombre, @NonNull Integer energia, @NonNull Ubicacion ubicacion) {
        super(nombre, energia, ubicacion);
    }

    @Override
    public boolean estaEndemoniado() {
        return true;
    }

    @Override
    public boolean puedeExorcisar() {
        return false;
    }

    @Override
    public EspirituTipo getTipo() {
        return EspirituTipo.DEMONIO;
    }

    @Override
    public int getExorsismosEvitados() {
        return this.getExorcismosInvolucrados();
    }

    @Override
    public int getExorsismosResueltos() {
        return 0;
    }

    public void recibirAtaque(int cantidadDeEnergiaPerdida){
        this.perderEnergia(cantidadDeEnergiaPerdida);
        this.evaluarDesconexion();
    }

    private void evaluarDesconexion(){
        if (this.debeDesconectarse()){
            this.desconectarDeMedium();
        }
    }

    public boolean debeDesconectarse() {
        return this.getEnergia() <= 0;
    }

    private void desconectarDeMedium(){
            this.getMediumConectado().desconectarEspiritu(this);
            this.setNivelDeConexion(0);
            this.setMediumConectado(null);
            this.eliminar();
        }

    public void exorcismoFallido() {
        this.exorcismosInvolucrados+=1;
    }
}

