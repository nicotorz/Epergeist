package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.EspirituOcupadoException;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.exception.MediumSinSuficienteManaException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MediumServiceTest {

    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private HabilidadService habilidadService;

    private Medium fabri;
    private Medium nico;
    private Espiritu espiritu1;
    private Espiritu espiritu2;
    private Ubicacion iglesia;
    private Ubicacion rio;

    @BeforeEach
    public void setUp()  {

        iglesia = new Santuario("Iglesia santa", 40);
        rio = new Cementerio("Rio", 20);

        ubicacionService.guardar(iglesia);
        ubicacionService.guardar(rio);

        fabri = new Medium("fabri", 100, 0, iglesia);
        nico = new Medium("nico", 100, 20, iglesia);

        espiritu1 = new EspirituAngelical("Jeremias", 50, iglesia);
        espiritu2 = new EspirituDemoniaco(  "Slenderman",50, rio);

        espiritu2.setNivelDeConexion(90);
        espiritu1.setNivelDeConexion(66);

        mediumService.guardar(fabri);
        mediumService.guardar(nico);
        espirituService.guardar(espiritu1);
        espirituService.guardar(espiritu2);

    }

    @Test
    void cuandoUnMediumDescansaEnUnSantuarioRecupera150PorcientoManaDeLaEnergiaQueProveeDichoSantuario() {
        fabri.conectarseAEspiritu(espiritu1);
        fabri.conectarseAEspiritu(espiritu2);
        mediumService.guardar(fabri);

        mediumService.descansar(fabri.getId());
        Integer manaEsperado = (int) (fabri.getMana() + (fabri.getUbicacion().getEnergia() * 1.50));

        Medium mediumDescansado = mediumService.recuperar(fabri.getId());

        assertEquals(mediumDescansado.getMana(), manaEsperado);
    }

    @Test
    void cuandoUnMediumDescansaEnUnCementerioRecupera50PorcientoManaDeLaEnergiaQueProveeDichoCementerio() {
        fabri.moverse(rio);
        fabri.conectarseAEspiritu(espiritu1);
        fabri.conectarseAEspiritu(espiritu2);
        mediumService.guardar(fabri);

        mediumService.descansar(fabri.getId());
        Integer manaEsperado = (int) (fabri.getMana() + (fabri.getUbicacion().getEnergia() * 0.50));

        Medium mediumDescansado = mediumService.recuperar(fabri.getId());

        assertEquals(mediumDescansado.getMana(), manaEsperado);
    }

    @Test
    void descansarManaNoExcedeElMaximoDe100PuntosDeMana() {
        fabri.setMana(90);
        mediumService.guardar(fabri);

        mediumService.descansar(fabri.getId());

        Medium mediumDescansado = mediumService.recuperar(fabri.getId());

        assertEquals(mediumDescansado.getMana(), 100);
    }

    @Test
    void losEspiritusConectadosAlMediumRecuperanEnergiaSegunLaUbicacionCuandoElMediumDescansa() {
        fabri.conectarseAEspiritu(espiritu1);
        fabri.conectarseAEspiritu(espiritu2);
        mediumService.guardar(fabri);

        mediumService.descansar(fabri.getId());

        Medium recuperado = mediumService.recuperar(fabri.getId());
        Espiritu angel = espirituService.recuperar(espiritu1.getId());
        Espiritu demonio = espirituService.recuperar(espiritu2.getId());

        assertEquals(demonio.getEnergia(), 50 ); // Espiritu que no recupero energia por que la ubicacion es un santuario
        assertEquals(angel.getEnergia(), 90 ); // Espiritu que recupero energia por que la ubicacion es un santuario
        assertEquals(recuperado.getEspiritus().size(), 2);
    }

    @Test
    void seSabeLosEspiritusQueTieneConectadoElMediumDado(){
        fabri.conectarseAEspiritu(espiritu1);
        fabri.conectarseAEspiritu(espiritu2);
        mediumService.guardar(fabri);

        Medium recuperado = mediumService.recuperar(fabri.getId());

        List<Espiritu> espiritus = mediumService.espiritus(recuperado.getId());

        for (Espiritu espiritu : espiritus) {
            assertEquals(recuperado.getId(), espiritu.getMediumConectado().getId());
        }
        assertEquals(fabri.getEspiritus().size(), 2);
    }

    @Test
    void unMediumPuedeInvocarAUnEspirituLibreSiTieneMasDe10DeMana() {
        // Exercise
        int manaActual = nico.getMana();
        nico.conectarseAEspiritu(espiritu2);
        mediumService.guardar(nico);

        mediumService.invocar(nico.getId(), espiritu1.getId());

        // Recupero al medium y espiritu para reflejar los cambios en la db
        Medium mediumInvocador = mediumService.recuperar(nico.getId());
        Espiritu espirituInvocado = mediumService.recuperarEspiritu(espiritu1.getId());

        // Assert
        assertEquals(mediumInvocador.getUbicacion().getId(), espirituInvocado.getUbicacion().getId());
        assertEquals(mediumInvocador.getMana(), manaActual - 10);
    }

    @Test
    void unMediumConMenosDe10DeManaNoPuedeRealizarLaInvocacionDeUnEspiritu() {
        // Recupero el medium y al espiritu
        Medium mediumInvocador = mediumService.recuperar(fabri.getId());
        Espiritu espirituInvocado = mediumService.recuperarEspiritu(espiritu1.getId());

        // Exercise+Assert
        assertThrows(MediumSinSuficienteManaException.class, () -> {
            mediumService.invocar(mediumInvocador.getId(), espirituInvocado.getId());
        });
    }

    @Test
    void unMediumNoPuedeRealizarLaInvocacionDeUnEspirituQueNoEstaLibre() {
        // Se conecta espiritu1 a fabri, por lo que espiritu1 ya no esta libre
        fabri.conectarseAEspiritu(espiritu1);
        // Actualizo para reflejar los cambios en la db
        mediumService.guardar(fabri);

        // Recupero el medium y al espiritu
        Medium mediumInvocador = mediumService.recuperar(nico.getId());
        Espiritu espirituInvocado = mediumService.recuperarEspiritu(espiritu1.getId());

        // Exercise+Assert
        assertThrows(EspirituOcupadoException.class, () -> {
            mediumService.invocar(mediumInvocador.getId(), espirituInvocado.getId());
        });
    }

    @Test
    void unMediumIntentaExorcizarAOtroSinTenerEspiritusAngelicales(){
        Long idExorcista = fabri.getId();
        Long idAExorcisar = nico.getId();

        assertThrows(ExorcistaSinAngelesException.class, () ->{
            mediumService.exorcizar(idExorcista, idAExorcisar);
        });
    }

    @Test
    void unMediumRealizaUnExorcismoExitoso(){
        Long idExorcista = fabri.getId();
        Long idAExorcisar = nico.getId();

        fabri.conectarseAEspiritu(espiritu1); //espiritu angelical
        nico.conectarseAEspiritu(espiritu2); //espiritu demonio

        mediumService.guardar(fabri);
        mediumService.guardar(nico);

        mediumService.exorcizar(idExorcista, idAExorcisar);

        //recupero los espiritus despues de realizar el exorcizar
        Espiritu esp = mediumService.recuperarEspiritu(espiritu1.getId());
        Espiritu espirituDemonio = mediumService.recuperarEspiritu(espiritu2.getId());

        assertEquals(30, esp.getEnergia());
        assertEquals(0, espirituDemonio.getEnergia());
        assertEquals(1, esp.getExorcismosInvolucrados());
    }
    @Test
    void unMediumIntentaExorcizarAOtroPeroEsFallidoSeLeDescuentaEnergiaIgualmente(){
        Long idExorcista = fabri.getId();
        Long idAExorcisar = nico.getId();

        espiritu1.setNivelDeConexion(10);

        fabri.conectarseAEspiritu(espiritu1); //espiritu angelical
        nico.conectarseAEspiritu(espiritu2); //espiritu demonio

        mediumService.guardar(fabri);
        mediumService.guardar(nico);
        int energiaAntesDeExorcizar = espiritu2.getEnergia();

        mediumService.exorcizar(idExorcista, idAExorcisar);

        //recupero los espiritus despues de realizar el exorcizar
        Espiritu esp = mediumService.recuperarEspiritu(espiritu1.getId());
        Espiritu espirituDemonio = mediumService.recuperarEspiritu(espiritu2.getId());

        assertEquals(0, esp.getEnergia());
        assertEquals(energiaAntesDeExorcizar, espirituDemonio.getEnergia());
        assertEquals(1, espirituDemonio.getExorcismosInvolucrados());
    }

    @Test
    void cuandoUnMediumIntentaExorcisarAOtroSuEspirituDemoniacoQuedaConEnergiaEnCeroYSeDesconectaDelMedium(){
        Long idExorcista = fabri.getId();
        Long idAExorcisar = nico.getId();
        espiritu2.setEnergia(5);

        fabri.conectarseAEspiritu(espiritu1); //espiritu angelical
        nico.conectarseAEspiritu(espiritu2); //espiritu demonio

        mediumService.guardar(fabri);
        mediumService.guardar(nico);

        mediumService.exorcizar(idExorcista, idAExorcisar);

        Medium nico2 = mediumService.recuperar(nico.getId());
        Espiritu espirituDemonio = mediumService.recuperarEspiritu(espiritu2.getId());


        assertEquals(nico2.getEspiritus().size(), 0);
        assertEquals(0, espirituDemonio.getEnergia());
        assertEquals(0, espirituDemonio.getNivelDeConexion());
        assertNotNull(espirituDemonio);
        assertEquals(espirituDemonio.getFechaEliminado(), LocalDate.now());
    }

    @Test
    void unMediumPuedeMoverseDeUbicacion() {
        Long idMedium = nico.getId();
        Long idUbicacion = rio.getId();
        nico.conectarseAEspiritu(espiritu1);
        nico.conectarseAEspiritu(espiritu2);

        mediumService.guardar(nico);

        mediumService.mover(idMedium, idUbicacion);

        Medium mediumRecuperado = mediumService.recuperar(idMedium);

        assertEquals(mediumRecuperado.getUbicacion().getId(), idUbicacion);
    }

    @Test
    void cuandoUnMediumSeMueveDeUbicacionTodosSusEspiritusConectadosSeMuevenConEl() {
        Long idMedium = nico.getId();
        Long idUbicacion = rio.getId();
        nico.conectarseAEspiritu(espiritu1);
        nico.conectarseAEspiritu(espiritu2);

        mediumService.guardar(nico);

        mediumService.mover(idMedium, idUbicacion);

        Medium mediumRecuperado = mediumService.recuperar(idMedium);

        for(Espiritu espiritu : mediumRecuperado.getEspiritus()) {
            assertEquals(espiritu.getUbicacion().getId(), idUbicacion);
        }
    }

    @Test
    void cuandoSeRealizaUnExorcismoExitosoMutanLasHabilidadesDeLosEspiritus(){
        Long idExorcista = fabri.getId();
        Long idAExorcisar = nico.getId();

        Habilidad materializacion = new Habilidad("Materializacion");
        Habilidad mutacionSobreSiMismo = new Habilidad("MutacionSobreSiMismo");

        habilidadService.crear(materializacion);
        habilidadService.crear(mutacionSobreSiMismo);

        habilidadService.descubrirHabilidad(materializacion.getNombre(),mutacionSobreSiMismo.getNombre(), new Condicion(10, Evaluacion.ENERGIA));

        espiritu2.setUbicacion(nico.getUbicacion());
        espirituService.guardar(espiritu2);

        espirituService.agregarHabilidadAEspiritu(espiritu1.getId(), materializacion.getId());

        espirituService.conectar(espiritu1.getId(), fabri.getId()); //espiritu angelical
        espirituService.conectar(espiritu2.getId(), nico.getId()); //espiritu demonio

        mediumService.guardar(fabri);
        mediumService.guardar(nico);

        mediumService.exorcizar(idExorcista, idAExorcisar);

        //recupero los espiritus despues de realizar el exorcizar
        Espiritu esp = mediumService.recuperarEspiritu(espiritu1.getId());
      //  Espiritu espirituDemonio = mediumService.recuperarEspiritu(espiritu2.getId());

        assertEquals(1, esp.getHabilidades().size());
    }


    @AfterEach
    void eliminarDAO() {
        mediumService.eliminarTodo();
        espirituService.eliminarTodo();
        ubicacionService.eliminarTodo();
        habilidadService.clearAll();
    }
}
