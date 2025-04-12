package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.HabilidadesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.exception.MutacionImposibleException;
import ar.edu.unq.epersgeist.persistencia.dto.HabilidadNeo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HabilidadServiceTest {
    @Autowired
    HabilidadService habilidadService;

    @Autowired
    EspirituService espirituService;

    @Autowired
    UbicacionService   ubicacionService;




    private Ubicacion ubicacion;
    private Espiritu espiritu;

    private Habilidad materializacion;
    private Habilidad mutacionSobreSiMismo;
    private Habilidad ilusiones;
    private Habilidad absorcion;
    private Habilidad manipulacion;
    private Habilidad controlSobreEspiritus;
    private Habilidad expansionDominio;
    private Habilidad resonanciaDeALmas;
    @BeforeEach
    public void setUp() {

        materializacion = new Habilidad("Materializacion");
        mutacionSobreSiMismo = new Habilidad("MutacionSobreSiMismo");
        ilusiones = new Habilidad("Ilusiones");
        absorcion = new Habilidad("Absorcion");
        manipulacion = new Habilidad("Manipulacion");
        controlSobreEspiritus = new Habilidad("ControlSobreEspiritus");
        expansionDominio = new Habilidad("ExpansionDominio");
        resonanciaDeALmas = new Habilidad("ResonanciaDeAlmas");


        ubicacion = new Cementerio("Ubicacion",50);
        espiritu = new EspirituAngelical("Inky", 20, ubicacion);
        espiritu.setNivelDeConexion(85);

        ubicacionService.guardar(ubicacion);
        espirituService.guardar(espiritu);


        habilidadService.crear(materializacion);
        habilidadService.crear(mutacionSobreSiMismo);
        habilidadService.crear(ilusiones);
        habilidadService.crear(absorcion);
        habilidadService.crear(manipulacion);
        habilidadService.crear(controlSobreEspiritus);
        habilidadService.crear(expansionDominio);
        habilidadService.crear(resonanciaDeALmas);

        habilidadService.descubrirHabilidad(materializacion.getNombre(),mutacionSobreSiMismo.getNombre(), new Condicion(10, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(materializacion.getNombre(),ilusiones.getNombre(), new Condicion(10, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(ilusiones.getNombre(),mutacionSobreSiMismo.getNombre(),new Condicion(10, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(mutacionSobreSiMismo.getNombre(),absorcion.getNombre(),new Condicion(10, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(mutacionSobreSiMismo.getNombre(),resonanciaDeALmas.getNombre(),new Condicion(10, Evaluacion.ENERGIA));


        habilidadService.descubrirHabilidad(ilusiones.getNombre(),manipulacion.getNombre(),new Condicion(10, Evaluacion.EXORCISMOSRESUELTOS));
        habilidadService.descubrirHabilidad(absorcion.getNombre(),controlSobreEspiritus.getNombre(),new Condicion(50, Evaluacion.NIVELDECONEXION));
        habilidadService.descubrirHabilidad(manipulacion.getNombre(),controlSobreEspiritus.getNombre(),new Condicion(90, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(controlSobreEspiritus.getNombre(),expansionDominio.getNombre(),new Condicion(90, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(ilusiones.getNombre(),absorcion.getNombre(),new Condicion(10, Evaluacion.EXORCISMOSEVITADOS));

        espirituService.agregarHabilidadAEspiritu(espiritu.getId(), materializacion.getId());
    }



    @Test
    void TestUnEspirituTieneMasDeUnaHabilidad(){

        espirituService.agregarHabilidadAEspiritu(espiritu.getId(), mutacionSobreSiMismo.getId());
        espirituService.agregarHabilidadAEspiritu(espiritu.getId(), ilusiones.getId());
        assertEquals(espirituService.recuperar(espiritu.getId()).getHabilidades().size(),3);
    }

    @Test
    void TestDeAgregarCondicon() {

        Habilidad habilidadtest = new Habilidad("Intocable");
        habilidadService.crear(habilidadtest);

        habilidadService.descubrirHabilidad(habilidadtest.getNombre(),mutacionSobreSiMismo.getNombre(), new Condicion(10, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(habilidadtest.getNombre(),ilusiones.getNombre(), new Condicion(10, Evaluacion.ENERGIA));
        habilidadService.descubrirHabilidad(ilusiones.getNombre(),mutacionSobreSiMismo.getNombre(), new Condicion(10, Evaluacion.NIVELDECONEXION));

        Habilidad habilidad = habilidadService.recuperar(habilidadtest.getNombre());
        assertEquals(habilidad.getCondicion().size() ,2);

    }

    @Test
    void TestMutarEspiritu(){

        assertEquals(espirituService.recuperar(espiritu.getId()).getHabilidades().size(),1);
        habilidadService.evolucionar(espiritu.getId());
        assertEquals(espirituService.recuperar(espiritu.getId()).getHabilidades().size(),3);
    }

    @Test
    void TestHabilidadesConectadas() {
        Set<Habilidad> habilidades = habilidadService.habilidadesConectadas(materializacion.getNombre());

        assertEquals(habilidades.size() ,2);
        assertTrue(habilidades.stream().anyMatch(habilidad -> habilidad.getNombre().equals(ilusiones.getNombre())));
        assertTrue(habilidades.stream().anyMatch(habilidad -> habilidad.getNombre().equals(mutacionSobreSiMismo.getNombre())));
    }


    @Test
    void TestHabilidadesPosibles(){

        espiritu.setEnergia(10);
        espiritu.setNivelDeConexion(0);
        espirituService.guardar(espiritu);
        espirituService.agregarHabilidadAEspiritu(espiritu.getId(), mutacionSobreSiMismo.getId());


        Set<Habilidad> habilidadesPosibles = habilidadService.habilidadesPosibles(espiritu.getId());
        assertEquals(habilidadesPosibles.size(),3);


    }

    @Test
    void TestHabilidadesPosiblesDevuelveTodoElArbol(){

        espiritu.setEnergia(100);
        espiritu.setNivelDeConexion(100);
        espiritu.setExorcismosInvolucrados(100);
        espirituService.guardar(espiritu);
        espirituService.agregarHabilidadAEspiritu(espiritu.getId(), mutacionSobreSiMismo.getId());


        Set<Habilidad> habilidadesPosibles = habilidadService.habilidadesPosibles(espiritu.getId());
        assertEquals(habilidadesPosibles.size(),6);

    }


    @Test
    void TestCaminoMasRentableCasoPositivo(){
        Set<Evaluacion> condicionSet = new HashSet<>();
        condicionSet.add(Evaluacion.ENERGIA);

        List<Habilidad> caminoMenosMutable = habilidadService.caminoMasRentable(materializacion.getNombre(),absorcion.getNombre(),condicionSet);
        assertEquals(caminoMenosMutable.size(),3);
        assertEquals(caminoMenosMutable.getFirst().getNombre(), materializacion.getNombre());
        assertEquals(caminoMenosMutable.get(1).getNombre(), mutacionSobreSiMismo.getNombre());
        assertEquals(caminoMenosMutable.get(2).getNombre(), absorcion.getNombre());
    }


    @Test
    void TestCaminoMasRentableCasoMutacionImposibleException(){

        Set<Evaluacion> condicionSet = new HashSet<>();
        condicionSet.add(Evaluacion.EXORCISMOSEVITADOS);
        condicionSet.add(Evaluacion.EXORCISMOSRESUELTOS);

        assertThrows(MutacionImposibleException.class, () -> {
            habilidadService.caminoMasRentable(ilusiones.getNombre(),controlSobreEspiritus.getNombre(),condicionSet);
        });

    }

    @Test
    void TestCaminoMasRentableCasoHabilidadesNoConectadasException(){

        Set<Evaluacion> condicionSet = new HashSet<>();
        condicionSet.add(Evaluacion.ENERGIA);
        condicionSet.add(Evaluacion.EXORCISMOSEVITADOS);
        condicionSet.add(Evaluacion.EXORCISMOSRESUELTOS);
        condicionSet.add(Evaluacion.NIVELDECONEXION);

        assertThrows(HabilidadesNoConectadasException.class, () -> {
            habilidadService.caminoMasRentable(mutacionSobreSiMismo.getNombre(),ilusiones.getNombre(),condicionSet);
        });

    }

    @Test
    void TestBonusCaminoMasMutable(){ //BONUS 1

        List<Habilidad> caminoMasMutable = habilidadService.caminoMasMutable(espiritu.getId(),mutacionSobreSiMismo.getNombre());
        assertEquals(caminoMasMutable.size(),2);
        assertEquals(caminoMasMutable.getFirst().getNombre(), absorcion.getNombre());
        assertEquals(caminoMasMutable.get(1).getNombre(), controlSobreEspiritus.getNombre());


    }

    @Test
    void TestBonusCaminoMenosMutable(){ //BONUS 2

        List<Habilidad> caminoMasMutable = habilidadService.caminoMenosMutable(mutacionSobreSiMismo.getNombre(),espiritu.getId());
        assertEquals(caminoMasMutable.size(),1);
    }

    @AfterEach
    void tearDown() {
        espirituService.eliminarTodo();
        habilidadService.clearAll();
        ubicacionService.eliminarTodo();
        
    }
}
