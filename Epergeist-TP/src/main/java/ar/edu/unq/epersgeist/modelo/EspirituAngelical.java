package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.EspirituOcupadoException;
import ar.edu.unq.epersgeist.modelo.exception.SoloSePuedenInvocarDemoniosEnUnCementerio;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Random;

@NoArgsConstructor
public class EspirituAngelical extends Espiritu {


    public EspirituAngelical( @NonNull String nombre, @NonNull Integer energia,@NonNull Ubicacion ubicacion ) {
        super(nombre, energia, ubicacion);
    }

    @Override
    public boolean estaEndemoniado() {
        return false;
    }

    @Override
    public boolean puedeExorcisar() {
        return true;
    }

    @Override
    public EspirituTipo getTipo() {
        return EspirituTipo.ANGELICAL;
    }

    @Override
    public int getExorsismosEvitados() {
        return 0;
    }

    @Override
    public int getExorsismosResueltos() {
        return this.getExorcismosInvolucrados();
    }

    public void atacar(EspirituDemoniaco objetivo){
        int porcentajeExito = calcularPorcentajeExito();

        if (porcentajeExito > 66){
            //Ataque exitoso

            this.perderEnergia(10);
            objetivo.recibirAtaque(this.getNivelDeConexion() / 2);
            // el demonio pierde energia a la mitad del nivel de conexion del angel

        }else {
            //Ataque fallido
            this.perderEnergia(10); // pierde 10 de energia

        }
    }

    public void exoricismoExitoso() {
        this.mutarAHabilidades();
        this.exorcismosInvolucrados+=1;
    }

    protected int calcularPorcentajeExito(){
        Random random = new Random();
        int numeroAleatorio = random.nextInt(10) + 1;

        return  numeroAleatorio + this.getNivelDeConexion();
    }
}
