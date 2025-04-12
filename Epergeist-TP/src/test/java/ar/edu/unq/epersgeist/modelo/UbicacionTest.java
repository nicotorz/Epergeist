package ar.edu.unq.epersgeist.modelo;


import ar.edu.unq.epersgeist.modelo.exception.SoloSePuedenInvocarAngelesEnUnSatuario;
import ar.edu.unq.epersgeist.modelo.exception.SoloSePuedenInvocarDemoniosEnUnCementerio;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UbicacionTest {


    private Ubicacion ubicacionTest;
    private Santuario santuario;
    private Cementerio cementerio;
    private EspirituDemoniaco demonio;
    private EspirituAngelical angelical;
    private Medium medium;

    @BeforeEach
    void setUp() {
        santuario = new Santuario("Lujan", 50);
        cementerio = new Cementerio("Ezpeleta", 50);
        demonio    = new EspirituDemoniaco("Demon",10,cementerio);
        angelical  = new EspirituAngelical("Angel",10,santuario);
        medium     = new Medium("Migue",100,10,santuario);
    }

    @Test
    void unaUbicacionPuedeDevolverSuNombre(){
        assertEquals(santuario.getNombre(),"Lujan");
    }

    @Test
    void unaUbicacionPuedeDecirSiEsSantuario(){
        assertTrue(santuario.esSantuario());
        assertFalse(cementerio.esSantuario());
    }

    @Test
    void unaUbicacionValidaUnaInvocacionCementerio(){
        assertThrows(SoloSePuedenInvocarDemoniosEnUnCementerio.class, () -> {
            cementerio.validarInvocacion(angelical);
        });

        assertDoesNotThrow(() -> {
            cementerio.validarInvocacion(demonio);
        });

    }

    @Test
    void unaUbicacionValidaUnaInvocacionSantuario(){
        assertThrows(SoloSePuedenInvocarAngelesEnUnSatuario.class, () -> {
            santuario.validarInvocacion(demonio);
        });

        assertDoesNotThrow(() -> {
            santuario.validarInvocacion(angelical);
        });
    }

    @Test
    void unaUbicacionIndicaQueTipoEs(){
        assertEquals(cementerio.getTipo() , UbicacionTipo.CEMENTERIO);
        assertEquals(santuario.getTipo() , UbicacionTipo.SANTUARIO);
    }


    @Test
    void unaUbicacionTipoCementerioLeRestaEnergiaALosEspiriutusNoDemoniacos(){
        assertEquals(demonio.getEnergia() ,10);
        assertEquals(angelical.getEnergia() , 10);

        cementerio.actualizarEnergia(angelical);
        cementerio.actualizarEnergia(demonio);
        assertEquals(demonio.getEnergia() ,10);
        assertEquals(angelical.getEnergia() , 5);
    }

    @Test
    void unaUbicacionTipoSantuarioLeRestaEnergiaALosEspiriutusNoAngelicales(){
        assertEquals(demonio.getEnergia() ,10);
        assertEquals(angelical.getEnergia() , 10);

        santuario.actualizarEnergia(angelical);
        santuario.actualizarEnergia(demonio);
        assertEquals(demonio.getEnergia() ,0);
        assertEquals(angelical.getEnergia() , 10);
    }

    @Test
    void enUnCementerioSoloLosDemoniosRecuperanEnergia(){

        medium.conectarseAEspiritu(demonio);
        medium.conectarseAEspiritu(angelical);
        assertEquals(demonio.getEnergia() ,10);
        assertEquals(angelical.getEnergia() , 10);

        cementerio.descansarEnUbicacion(medium);

        assertEquals(demonio.getEnergia() ,60);
        assertEquals(angelical.getEnergia() , 10);
    }

    @Test
    void enUnSantuarioSoloLosAngelesRecuperanEnergia(){

        medium.conectarseAEspiritu(demonio);
        medium.conectarseAEspiritu(angelical);
        assertEquals(demonio.getEnergia() ,10);
        assertEquals(angelical.getEnergia() , 10);

        santuario.descansarEnUbicacion(medium);

        assertEquals(demonio.getEnergia() ,10);
        assertEquals(angelical.getEnergia() , 60);
    }

    @Test
    void unMediumRecuperaEnergiaDependiendoDeLaUbicacion_Cemeneterio(){
        assertEquals(medium.getMana(),10);
        cementerio.descansarEnUbicacion(medium);
        assertEquals(medium.getMana(),35);
    }

    @Test
    void unMediumRecuperaEnergiaDependiendoDeLaUbicacion_Santuario(){
        assertEquals(medium.getMana(),10);
        santuario.descansarEnUbicacion(medium);
        assertEquals(medium.getMana(),85);
    }











}
