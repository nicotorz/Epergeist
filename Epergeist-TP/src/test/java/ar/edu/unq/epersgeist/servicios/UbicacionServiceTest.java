package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UbicacionServiceTest {

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private EspirituService espirituService;

    private Ubicacion ubicacion;
    private Espiritu espiritu;
    private Medium medium;

    private Medium medium2;
    private Medium medium3;

    @BeforeEach
    public void setUp() {

        ubicacion = new Santuario("Roma", 50);

        espiritu = new EspirituAngelical("ElDiegooo",86, ubicacion);

        medium = new Medium("Messi",10,10, ubicacion);

        ubicacionService.guardar(ubicacion);
        espirituService.guardar(espiritu);
        mediumService.guardar(medium);
        espirituService.conectar(espiritu.getId(),medium.getId());
    }

    @Test
    void elServicePuedeTraerUnaUbicacionDeLaBaseDeDatos() {

        Ubicacion ubicacionDeLaBase = ubicacionService.recuperar(ubicacion.getId());

        assertEquals(ubicacion.getId(), ubicacionDeLaBase.getId());

    }


    @Test
    void elServiceTraeTodasLasUbicacionesDeLaBaseDeDatos() {
        Ubicacion ubicacion2 = new Santuario("Jujuy", 50);
        ubicacionService.guardar(ubicacion2);

        List<Ubicacion> ubicacionesDeLaBaseDeDatos= ubicacionService.recuperarTodos();

        assertEquals(2, ubicacionesDeLaBaseDeDatos.size());
    }

    @Test
    void elServicePuedeActualizarUnaUbicacionDeLaBaseDeDatos() {
        Ubicacion ubicacionDeLaBase;

        //Le cambio el nombre al objeto de la ubicacion
        ubicacion.setNombre("Roman't");

        //Veo que no me lo cambio en la base de datos
        ubicacionDeLaBase = ubicacionService.recuperar(ubicacion.getId());
        assertNotEquals(ubicacion.getNombre(), ubicacionDeLaBase.getNombre());

        //Actualizo la base de datos
        ubicacionService.guardar(ubicacion);

        ubicacionDeLaBase = ubicacionService.recuperar(ubicacion.getId());
        assertEquals(ubicacion.getNombre(), ubicacionDeLaBase.getNombre());
    }

    @Test
    void elServicePuedeEliminarUnaUbicacionDeLaBaseDeDatos() {
        Ubicacion ubicacionParaEliminar = new Cementerio("Yemen", 60);
        ubicacionService.guardar(ubicacionParaEliminar);

        //El recuperar me trea null
        ubicacionService.eliminar(ubicacionParaEliminar);
        assertThrows(NoSuchElementException.class, () ->{
            ubicacionService.recuperar(ubicacionParaEliminar.getId());
        });
    }

    @Test
    void elServicePuedeTraerTodosLosEspiritusDeUnaUbicacionDeLaBaseDeDatos() {
        Ubicacion ubicacionParaHacerBulto = new Cementerio("Yemen", 60);
        Espiritu espirituParaRellenar = new EspirituDemoniaco("Pele" ,3, ubicacion);

        espirituService.guardar(espirituParaRellenar);

        ubicacionService.guardar(ubicacionParaHacerBulto);
        ubicacionService.guardar(ubicacion);
        List <Espiritu> espiritusEnUbicacion = ubicacionService.espiritusEn(ubicacion.getId());

        Espiritu espirituTraido = ubicacionService.espiritusEn(ubicacion.getId()).getFirst();

        assertEquals(espirituTraido.getId(),espiritu.getId());
        assertEquals(espiritusEnUbicacion.size(),2 );

    }

    @Test
    void elServicePuedeTraerTodosLosMediumDeUnaUbicacionSinEspiritusDeLaBaseDeDatos() {
        Medium sinEspiritu = new Medium("Yo", 99,1, ubicacion);

        ubicacionService.guardar(ubicacion);
        mediumService.guardar(sinEspiritu);
        List<Medium> sinEspiritus = ubicacionService.mediumsSinEspiritusEn(ubicacion.getId());
        assertEquals(sinEspiritus.getFirst().getNombre(),"Yo" );
        assertEquals(sinEspiritus.size(),1 );

    }

    @Test

    void elSantuarioMasCorrupto(){

        // Crear santuarios
        Santuario santuario1 = new Santuario("Santuario 1", 50);
        Santuario santuario2 = new Santuario("Santuario 2", 50);

        medium2 = new Medium("Medium2",10,10, santuario1);


        EspirituDemoniaco demonio1 = new EspirituDemoniaco("Demonio 1", 100, santuario1);
        EspirituDemoniaco demonio2 = new EspirituDemoniaco("Demonio 2", 100, santuario1);
        EspirituAngelical angel1 = new EspirituAngelical("Angel 1", 80, santuario1);

        EspirituDemoniaco demonio3 = new EspirituDemoniaco("Demonio 3", 100, santuario2);
        EspirituAngelical angel2 = new EspirituAngelical("Angel 2", 80, santuario2);
        EspirituAngelical angel3 = new EspirituAngelical("Angel 3", 80, santuario2);


        ubicacionService.guardar(santuario1);
        ubicacionService.guardar(santuario2);

        espirituService.guardar(demonio1);
        espirituService.guardar(demonio2);
        espirituService.guardar(angel1);
        mediumService.guardar(medium2);
        espirituService.conectar(demonio1.getId(),medium2.getId());;

        espirituService.guardar(demonio3);
        espirituService.guardar(angel2);
        espirituService.guardar(angel3);



        ReporteSantuarioMasCorrupto reporte = ubicacionService.santuarioCorrupto();

       assertEquals("Santuario 1", reporte.getNombreSantuario());
       assertEquals(2, reporte.getTotalDemonios()); // Santuario 1 tiene 2 demonios
       assertEquals(1, reporte.getDemoniosLibres()); // Santuario 1 tiene 1 demonio sin medium
       assertEquals("Medium2", reporte.getMediumConMasDemonios().nombre());

    }

    @Test
    void seTraeAlMediumQueTieneMasManaEnCasoDeEmpatarLaCantidadDeDemonios(){
        Santuario santuario1 = new Santuario("Santuario 1", 50);

        medium2 = new Medium("Medium2",10,10, santuario1);
        medium3 = new Medium("Medium3",60,30, santuario1);

        EspirituDemoniaco demonio1 = new EspirituDemoniaco("Demonio 1", 100, santuario1);
        EspirituDemoniaco demonio2 = new EspirituDemoniaco("Demonio 2", 100, santuario1);


        ubicacionService.guardar(santuario1);

        espirituService.guardar(demonio1);
        espirituService.guardar(demonio2);
        mediumService.guardar(medium2);
        mediumService.guardar(medium3);

        espirituService.conectar(demonio1.getId(),medium2.getId());
        espirituService.conectar(demonio2.getId(),medium3.getId());



        ReporteSantuarioMasCorrupto reporte = ubicacionService.santuarioCorrupto();

        assertEquals(reporte.getMediumConMasDemonios().nombre(),"Medium3");
    }


    @AfterEach
    void limpiarBaseDeDatos() {
         espirituService.eliminarTodo();
         mediumService.eliminarTodo();
         ubicacionService.eliminarTodo();
    }


}
