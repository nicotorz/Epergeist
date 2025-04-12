package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.EspirituNoSeEncuentraEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituOcupadoException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public abstract class Espiritu {

    private Long id;
    @Min(1) @Max(100)
    private Integer nivelDeConexion;
    private String nombre;
    @Min(0) @Max(100)
    protected Integer energia;
    private LocalDate fechaEliminado;

    protected Ubicacion ubicacion;

    private Medium mediumConectado;

    private Set<Habilidad> habilidades= new HashSet<>();

    protected int exorcismosInvolucrados;

    public Espiritu(@NonNull String nombre, @NonNull Integer energia, @NonNull Ubicacion ubicacion) {
        this.nivelDeConexion = 0;
        this.nombre = nombre;
        this.setEnergia(energia);
        this.crearEspirituEn(ubicacion);
        this.exorcismosInvolucrados = 0;

    }

    public Medium conectar(Medium medium) {
        this.validarHabilitacion();
        this.validarConexion(medium);
        return this.aumentarConexion(medium);
    }

    public Medium aumentarConexion(Medium medium) {
        if(nivelDeConexion + 10 < 100 && medium.noTieneAlEspiritu(this)) {

            medium.conectarseAEspiritu(this);

            this.nivelDeConexion += (int) Math.round(medium.getMana() * 0.2);
            // El casteo lo hago para no cambiar el tipo de nivelDeConexion, podria cambiarlo a double
        }
        return  medium;
    }

    private void validarConexion(Medium medium) {
        this.estaEnLaMismaUbicacion(medium);
        this.validarDisponibilidad();
    }

    private void estaEnLaMismaUbicacion(Medium medium) {
        if (!this.ubicacion.equals(medium.getUbicacion())) {
            throw new EspirituNoSeEncuentraEnLaMismaUbicacionException(medium, this);
        }
    }

    public void validarDisponibilidad() {
        if(!this.estaLibre()) {
            throw new EspirituOcupadoException(this);
        }
    }

    public void perderEnergia(int cantidad) {
        energia = max(this.energia - cantidad, 0);
    }

    public boolean estaLibre() {
       return this.mediumConectado == null;

    }

    public abstract boolean estaEndemoniado();

    public abstract boolean puedeExorcisar();

    public void recuperarEnergia(int energiaARecuperar) {
        energia = min(this.energia + energiaARecuperar, 100);
    }
  
    private void crearEspirituEn(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public abstract EspirituTipo getTipo();

    public void setUbicacion(Ubicacion ubicacion) {
        this.crearEspirituEn(ubicacion);
    }

    public void setEnergia(Integer energia) {
        this.energia = min(energia,100);
    }

    public void setNivelDeConexion(Integer nivelDeConexion) {
        this.nivelDeConexion = max(0,min(nivelDeConexion,100));
    }

    public void eliminar() {
        this.fechaEliminado = LocalDate.now();
    }

    public boolean estaEliminado() {
        return this.fechaEliminado != null;
    }

    public void validarHabilitacion() {
        if (this.estaEliminado()) {
            throw new RuntimeException("El espiritu esta eliminado!");
        }
    }

    public boolean seEncuentraEnSantuario() {
        return ubicacion.esSantuario();
    }

    public void mutarAHabilidad(Set<Habilidad> habilidades) {
            this.getHabilidades().addAll(habilidades);
    }

    public abstract int getExorsismosEvitados();

    public abstract int getExorsismosResueltos();


    public Set<String> nombreHablidades() {
        return this.getHabilidades().stream()
                .map(Habilidad::getNombre)
                .collect(Collectors.toSet());
    }

    public void mutarAHabilidades(){
        for (Habilidad habilidad : this.getHabilidades()){
            for (Condicion condicion : habilidad.getCondicion()){
                this.puedeEvolucionar(condicion);
            }
        }
    }

    private void puedeEvolucionar(Condicion condicion) {
        if(cumpleLaCondicion(condicion)){
            this.getHabilidades().add(condicion.getHabilidad().aModelo());
        }
    }

    private boolean cumpleLaCondicion(Condicion condicion) {
        return switch (condicion.getEvaluacion()){
            case ENERGIA -> this.getEnergia() >= condicion.getCantidadNecesaria();
            case NIVELDECONEXION -> this.getNivelDeConexion() >= condicion.getCantidadNecesaria();
            case EXORCISMOSEVITADOS -> this.getExorsismosEvitados() >= condicion.getCantidadNecesaria();
            case EXORCISMOSRESUELTOS -> this.getExorsismosResueltos() >= condicion.getCantidadNecesaria();
        };
    }
}