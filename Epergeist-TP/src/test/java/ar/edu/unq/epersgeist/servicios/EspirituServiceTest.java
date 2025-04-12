package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EspirituServiceTest {

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private MediumService mediumService;

    private EspirituAngelical angelical;
    private EspirituDemoniaco demoniaco;
    private EspirituDemoniaco demoniaco2;
    private Medium medium1;
    private Medium medium2;
    private Medium medium3;
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;
    private Ubicacion ubicacion3;

    @BeforeEach
    public void prepare() {

        ubicacion1 = new Cementerio("Quilmes", 50);
        ubicacion2 = new Santuario("Berazategui", 50);
        ubicacion3 = new Santuario("Bernal", 50);

        ubicacionService.guardar(ubicacion1);
        ubicacionService.guardar(ubicacion2);
        ubicacionService.guardar(ubicacion3);

        medium1 = new Medium("Medium1", 50,50, ubicacion1);
        medium2 = new Medium("Medium2", 50,50, ubicacion2);
        medium3 = new Medium("Medium3", 50,50, ubicacion3);

        mediumService.guardar(medium1);
        mediumService.guardar(medium2);
        mediumService.guardar(medium3);

        // Crear y guardar Espíritus
        angelical = new EspirituAngelical("Tyrel", 100, ubicacion1);
        demoniaco = new EspirituDemoniaco("Valak", 60, ubicacion2);
        demoniaco2 = new EspirituDemoniaco("Demoniaco", 40, ubicacion3);

        espirituService.guardar(angelical);
        espirituService.guardar(demoniaco);
        espirituService.guardar(demoniaco2);

    }


    @Test
    void testRecuperarEspirituAngelical() {

        EspirituAngelical recuperado = (EspirituAngelical) espirituService.recuperar(angelical.getId());
        assertNotNull(recuperado);
        assertEquals("Tyrel", recuperado.getNombre());
        assertEquals(0, recuperado.getNivelDeConexion());
        assertEquals(100, recuperado.getEnergia());

    }

    @Test
    void testRecuperarEspirituDemoniaco() {
        EspirituDemoniaco recuperado = (EspirituDemoniaco) espirituService.recuperar(demoniaco.getId());
        assertNotNull(recuperado);
        assertEquals("Valak", recuperado.getNombre());
        assertEquals(0, recuperado.getNivelDeConexion());
        assertEquals(60, recuperado.getEnergia());
    }

    @Test
    void testRecuperarTodos() {
        List<Espiritu> todosLosEspiritus = espirituService.recuperarTodos();
        assertEquals(3, todosLosEspiritus.size());
    }

    @Test
    void testEspiritusDemoniacos() {

        List<Espiritu> demoniacos = espirituService.recuperarEspiritusDemoniacos();
        assertEquals(2, demoniacos.size());

        assertEquals(demoniacos.get(0).getId(), demoniaco.getId());
        assertEquals(demoniacos.get(1).getId(), demoniaco2.getId());
    }

    @Test
    void testConectarEspirituMedium() {

        Medium conectado1 = espirituService.conectar(angelical.getId(), medium1.getId());
        assertNotNull(conectado1);
        assertEquals(medium1.getId(), conectado1.getId());

        Medium conectado2 = espirituService.conectar(demoniaco.getId(),medium2.getId());
        assertNotNull(conectado2);
        assertEquals(medium2.getId(), conectado2.getId());

        Medium conectado3 = espirituService.conectar(demoniaco2.getId(),medium3.getId());
        assertNotNull(conectado3);
        assertEquals(medium3.getId(), conectado3.getId());

    }

    @Test
    void testEliminarEspiritu() {
        espirituService.eliminar(angelical);

        assertEquals(angelical.getFechaEliminado(), LocalDate.now());
    }

    //TEST BONUS PAGINACION TP2
    @Test
    void testPaginacionElServicioPuedeTraerTodosLosEspiritusDemoniacosEnFormaPagina(){

        Espiritu demon3 = new EspirituDemoniaco("LasNintentdos", 20, ubicacion1);
        Espiritu demon4 = new EspirituDemoniaco("ElDeCorbata", 666, ubicacion2);

        espirituService.guardar(demon3);
        espirituService.guardar(demon4);

        List<Espiritu> demPag0 = espirituService.recuperarEspiritusDemoniacos(Direccion.DESCENDENTE, 0, 2); // Página 0, cantidad 2
        List<Espiritu> demPag1 = espirituService.recuperarEspiritusDemoniacos(Direccion.DESCENDENTE, 1, 2); // Página 1, cantidad 2

        assertEquals(demPag0.size(), 2);
        assertEquals(demPag0.get(0).getNombre(),"ElDeCorbata"); // El demonio con más energía tiene que ser el primero
        assertEquals(demPag1.get(1).getNombre(),"LasNintentdos");// El demonio con menos energía tiene que ser el ultimo de la ultima pagina
    }


    @AfterEach
    void cleanup() {
        mediumService.eliminarTodo();
        espirituService.eliminarTodo();
        ubicacionService.eliminarTodo();
    }

}
