package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.exception.MediumSinSuficienteManaException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@Getter @Setter @NoArgsConstructor

public class Medium implements Serializable {
    private Long id;
    private String nombre;
    private Integer manaMax;
    private Integer mana;


    private Ubicacion ubicacion;

    private Set<Espiritu> espiritus = new HashSet<>();

    public Medium(@NonNull String nombre, @NonNull Integer manaMax, @NonNull Integer mana, @NonNull Ubicacion ubicacion ) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.setMana(mana);
        this.nacerMediumEn(ubicacion);
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        espiritu.setMediumConectado(this);
        espiritus.add(espiritu);
    }

    public boolean noTieneAlEspiritu(Espiritu espiritu) {
        return !espiritus.contains(espiritu);
    }

    public int cantidadDeEspiritus() {
        return this.getEspiritusActivos().size();
    }

    public List<Espiritu> getEspiritusActivos() {
        return this.espiritus.stream()
                .filter(e -> !e.estaEliminado()) // Filtra los espíritus eliminados
                .collect(Collectors.toList());
    }

    public void descansar() {
        ubicacion.descansarEnUbicacion(this);
    }

    public void recuperarManaPorDescanso(int manaDeDescanso) {
        int manadespuesDelDescanso = Math.min(mana+manaDeDescanso,manaMax);
        this.setMana(manadespuesDelDescanso);
    }

    public void exorcizar(Medium mediumAExorcizar) {
        this.contieneEspiritusAngelicales();
        this.atacarADemonioDe(mediumAExorcizar);
    }

    private void contieneEspiritusAngelicales() {
        if(this.espiritusAngelicales().isEmpty()){
            throw new ExorcistaSinAngelesException(this);
        }
    }

    public List<EspirituAngelical> espiritusAngelicales(){
        return this.getEspiritusActivos().stream()
                .filter(Espiritu::puedeExorcisar)
                .map(e -> (EspirituAngelical) e)
                .collect(Collectors.toList());
    }

    public List<EspirituDemoniaco> espiritusDemonicos() {
        return this.getEspiritusActivos().stream()
                .filter(Espiritu::estaEndemoniado)
                .map(e -> (EspirituDemoniaco) e)
                .collect(Collectors.toList());
    }

    private void atacarADemonioDe( Medium mediumAExorcizar ) {
        //Lista de espirutos a atacar
        List<EspirituDemoniaco> demonios = mediumAExorcizar.espiritusDemonicos();

        for(EspirituDemoniaco demonioAAtacar : demonios){
            //La lista de espiritus que le queda energia la compruebo antes
            iniciarAtaqueContraDemonio(demonioAAtacar);
            this.sumarSiFueExorcismoExitosoOFaliido(demonioAAtacar);

        }
    }

    private void sumarSiFueExorcismoExitosoOFaliido(EspirituDemoniaco demonioAAtacar) {
        if(demonioAAtacar.estaEliminado()){
            this.espiritusAngelicales().forEach(EspirituAngelical::exoricismoExitoso);

        }else{
            demonioAAtacar.exorcismoFallido();
        }
    }

    private void iniciarAtaqueContraDemonio(EspirituDemoniaco demonioAAtacar) {
        List<EspirituAngelical> espiritusAngelicalesConEnergia = this.espiritusAngelicalesConEnergia();

        while (!espiritusAngelicalesConEnergia.isEmpty() && !demonioAAtacar.estaEliminado()) {

            atacarConAngelesADemonio(espiritusAngelicalesConEnergia, demonioAAtacar);
            espiritusAngelicalesConEnergia = this.espiritusAngelicalesConEnergia();
        }

    }

    private void atacarConAngelesADemonio(List<EspirituAngelical> espiritusAngelicalesConEnergia, EspirituDemoniaco demonioAAtacar) {
        for(EspirituAngelical espiritusAngelicalConEnergia : espiritusAngelicalesConEnergia){
            espiritusAngelicalConEnergia.atacar(demonioAAtacar);
            if (demonioAAtacar.estaEliminado()) {
                break;
            }
        }
    }

    private List<EspirituAngelical> espiritusAngelicalesConEnergia(){
        return this.espiritusAngelicales().stream()
                .filter(e -> e.getEnergia() >= 10)
                .collect(Collectors.toList());
    }

    public Espiritu invocar(Espiritu espiritu) {
        espiritu.validarHabilitacion();
        //Valida que el medium tenga mas de 10 de mana y que el espiritu este disponible(libre)
        validarInvocacion(espiritu, this.ubicacion);

        //Consume el mana necesario de la invocacion
        this.mana -= 10;

        // Invoca al espiritu en la ubicacion del medium
        espiritu.setUbicacion(ubicacion);

        return espiritu;
    }

    private void validarInvocacion(Espiritu espiritu, Ubicacion ubicacion) {
        espiritu.validarHabilitacion();
        espiritu.validarDisponibilidad();
        ubicacion.validarInvocacion(espiritu);
        if (mana < 10) {
            throw new MediumSinSuficienteManaException(this);
        }
    }

    private void nacerMediumEn(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void desconectarEspiritu(EspirituDemoniaco espiritu) {
        this.espiritus.remove(espiritu);
    }

    public void setMana(Integer mana) {
        this.mana = max(0,min(this.manaMax, mana));
    }

    public void moverse(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
        moverEspiritusConectados();
    }

    private void moverEspiritusConectados() {
        for (Espiritu espiritu : this.getEspiritusActivos()) {
            espiritu.setUbicacion(this.getUbicacion());
            this.getUbicacion().actualizarEnergia(espiritu);
        }
    }

}