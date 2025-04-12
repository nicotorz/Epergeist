package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {

    private Medium medium;
    private Medium medium2;
    private EspirituDemoniaco espiritu;
    private Espiritu espiritu2;
    private Ubicacion ubicacion;
    private Ubicacion ubicacion2;

    @BeforeEach
    public void setUp() {
        ubicacion = new Santuario("Ubicacion", 50);
        ubicacion2 = new Cementerio("Ubicacion2", 50);
        espiritu = new EspirituDemoniaco("Llorona", 50, ubicacion);
        espiritu2 = new EspirituAngelical("Jeremias", 50, ubicacion);
        medium = new Medium("Persona", 100, 0, ubicacion);
        medium2 = new Medium("Persona", 100, 0, ubicacion2);
    }

    @Test
    void unMediumRecienCreadoTieneUnNombreAsignado(){

        assertEquals("Persona", medium.getNombre());
    }

    @Test
    void unMediumRecienCreadoTiene0DeManaY10DeManaMaximo(){

        assertEquals(0, medium.getMana());
        assertEquals(100, medium.getManaMax());
    }

    @Test
    void seSabeLosEspiritusQueTieneConectadoElMediumDado(){
        medium.conectarseAEspiritu(espiritu);
        medium.conectarseAEspiritu(espiritu2);

        Set<Espiritu> espiritus = medium.getEspiritus();

        for (Espiritu espiritu : espiritus) {
            assertEquals(espiritu.getMediumConectado(), medium);
        }
        assertEquals(medium.getEspiritus().size(), 2);
    }

    @Test
    void unMediumSeConectaAUnEspirituQueNoSeEncuentraConectado(){

        assertTrue(medium.noTieneAlEspiritu(espiritu));

        medium.conectarseAEspiritu(espiritu);

        assertEquals(1, medium.cantidadDeEspiritus());
    }

    @Test
    void cuandoUnMediumDescansaEnUnSantuarioRecupera150PorcientoManaDeLaEnergiaQueProveeDichoSantuario() {
        // setup
        Integer manaEsperado = (int) (medium.getMana() + (medium.getUbicacion().getEnergia() * 1.50));
        medium.descansar();

        // exercise
        assertEquals(manaEsperado, medium.getMana());
    }

    @Test
    void cuandoUnMediumDescansaEnUnCementerioRecupera50PorcientoManaDeLaEnergiaQueProveeDichoCementerio() {
        // setup
        medium.moverse(ubicacion2);
        Integer manaEsperado = (int) (medium.getMana() + (medium.getUbicacion().getEnergia() * 0.50));
        medium.descansar();

        // exercise
        assertEquals(manaEsperado, medium.getMana());
    }

    @Test
    void unMediumPuedeInvocarAUnEspirituASuUbicacionSiTieneMasDe10DeMana() {
        // setup
        medium.descansar();
        // exercise
        medium.invocar(espiritu2);
        // assert
        assertEquals(medium.getUbicacion(), espiritu.getUbicacion());
    }

    @Test
    void unMediumNoPuedeInvocarAUnEspirituDemoniacoAUnSantuario() {
        // setup
        medium.descansar();
        // exercise
        assertThrows(SoloSePuedenInvocarAngelesEnUnSatuario.class, () ->{
            medium.invocar(espiritu);
        });
    }

    @Test
    void unMediumNoPuedeInvocarAUnEspirituAngelicalAUnCementerio() {
        // setup
        medium.descansar();
        medium.moverse(ubicacion2);
        // exercise
        assertThrows(SoloSePuedenInvocarDemoniosEnUnCementerio.class, () ->{
            medium.invocar(espiritu2);
        });
    }

    @Test
    void unMediumPierde10DeManaCuandoSeRealizaLaInvocacion() {
        // setup
        medium.descansar();
        Integer manaEsperado = medium.getMana() - 10;
        // exercise
        medium.invocar(espiritu2);
        // assert
        assertEquals(manaEsperado, medium.getMana());
    }

    @Test
    void unMediumConMenosDe10DeManaNoPuedeRealizarLaInvocacionDeUnEspiritu() {
        // Exercise+Assert
        assertThrows(MediumSinSuficienteManaException.class, () -> {
            medium.invocar(espiritu2);
        });
    }

    @Test
    void unEspirituQueNoEstaLibreNoPuedeSerInvocado() {
        // Setup
        medium.descansar();
        medium.conectarseAEspiritu(espiritu);

        // Exercise+Assert
        assertThrows(EspirituOcupadoException.class, () -> {
            medium.invocar(espiritu);
        });
    }


    @Test
    void unMediumIntentaExorcizarAOtroSinTenerEspiritusAngelicales(){
        assertThrows(ExorcistaSinAngelesException.class, () ->{
            medium.exorcizar(medium2);
        });
    }

    @Test
    void unMediumRealizaUnExorcismoExitoso(){
        espiritu.setNivelDeConexion(10);
        espiritu2.setNivelDeConexion(90);
        medium.conectarseAEspiritu(espiritu2); //espiritu angelical
        medium2.conectarseAEspiritu(espiritu); //espiritu demonio

        medium.exorcizar(medium2);

        assertEquals(30, espiritu2.getEnergia());
        assertEquals(0, espiritu.getEnergia());
    }

    @Test
    void unMediumRealizaUnExorcismoAMuchosDemoniosALaVez(){
        EspirituDemoniaco espiritu3 = new EspirituDemoniaco("Valek", 10, ubicacion);

        espiritu.setNivelDeConexion(10);
        espiritu2.setNivelDeConexion(90);
        medium.conectarseAEspiritu(espiritu2); //espiritu angelical
        medium2.conectarseAEspiritu(espiritu); //espiritu demonio
        medium2.conectarseAEspiritu(espiritu3);

        assertEquals(50, espiritu.getEnergia());
        assertEquals(50, espiritu.getEnergia());
        assertEquals(10, espiritu3.getEnergia());

        medium.exorcizar(medium2);

        assertEquals(20, espiritu2.getEnergia());
        assertEquals(0, espiritu.getEnergia());
        assertEquals(0, espiritu3.getEnergia());
    }

    @Test
    void unMediumRealizaUnExorcismoPeroSuEspirituAngelicalSeQuedaSinEnergia(){
        EspirituDemoniaco espiritu3 = new EspirituDemoniaco("Balrog", 100, ubicacion);

        espiritu3.setNivelDeConexion(10);
        espiritu2.setNivelDeConexion(66);
        espiritu2.setEnergia(25);
        medium.conectarseAEspiritu(espiritu2); //espiritu angelical
        medium2.conectarseAEspiritu(espiritu3);

        assertEquals(25, espiritu2.getEnergia());
        assertEquals(100, espiritu3.getEnergia());

        medium.exorcizar(medium2);

        assertEquals(5, espiritu2.getEnergia());
        assertEquals(34, espiritu3.getEnergia());

    }


    @Test
    void unMediumSePuedeDdesconcetarDeUnEspiritu(){
        medium.conectarseAEspiritu(espiritu2);
        medium.desconectarEspiritu(espiritu);

        assertTrue(medium.getEspiritus().contains(espiritu2));
    }

    @Test
    void unMediumPuedeMoverseDeUbicacion() {
        medium.moverse(ubicacion2);

        assertEquals(medium.getUbicacion(), ubicacion2);
    }

    @Test
    void cuandoUnMediumSeMueveSusEspiritusSeMuevenConEl() {
        medium.conectarseAEspiritu(espiritu);
        medium.conectarseAEspiritu(espiritu2);
        medium.moverse(ubicacion2);

        for (Espiritu espiritu : medium.getEspiritus()) {
            assertEquals(espiritu.getUbicacion(), ubicacion2);
        }
    }

    @Test
    void cuandoUnMediumSeMueveSusEspiritusSeMuevenConElYPierdenEnergiaSegunLaUbicacion() {
        medium.conectarseAEspiritu(espiritu);
        medium.conectarseAEspiritu(espiritu2);
        medium.moverse(ubicacion2);

        for (Espiritu espiritu : medium.getEspiritus()) {
            if(!espiritu.estaEndemoniado()) {
                assertNotEquals(espiritu.getEnergia(), 50);
            }
        }
    }

}
