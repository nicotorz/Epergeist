package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoSeEncuentraEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
public class EspirituControllerTest {


    @Autowired
    private EspirituService espirituService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private MockMVCEspirituController mockMVCEspirituController;

    @Autowired
    private MockMVCUbicacionController mockMVCUbicacionController;

    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;

    private Espiritu espiritu;
    private Espiritu espiritu1;

    private Medium medium;

    @BeforeEach
    void setUp()throws Throwable{
        ubicacion1 = new Santuario("Iglesia", 10);
        ubicacion2 = new Santuario("Santuario", 10);

        ubicacionService.guardar(ubicacion2);
        ubicacionService.guardar(ubicacion1);

        var ubicacion3 = this.clonarUbicacionConId(ubicacion2);
        var ubicacion4 = this.clonarUbicacionConId(ubicacion1);

        Medium medium1 = new Medium("Gabi",100,20, ubicacion3);
        mediumService.guardar(medium1);

        medium = this.clonarMediumConId(medium1);

        Espiritu espiritu2 = new EspirituAngelical("Jeremias", 10, ubicacion3);
        Espiritu espirituNico = new EspirituAngelical("Nico", 30,ubicacion4);
        espirituService.guardar(espiritu2);
        espirituService.guardar(espirituNico);

        espiritu = this.clonarEspirituConId(espiritu2);
        espiritu1 = this.clonarEspirituConId(espirituNico);

    }

    private Ubicacion clonarUbicacionConId(Ubicacion ubicacion){
        var ubicacionAClonar = new Cementerio(ubicacion.getNombre(),ubicacion.getEnergia());
        ubicacionAClonar.setId(ubicacion.getId());
        return ubicacionAClonar;
    }

    private Medium clonarMediumConId(Medium medium){
        var mediumAClonar = new Medium(medium.getNombre(), medium.getManaMax(), medium.getMana(), medium.getUbicacion());
        mediumAClonar.setId(medium.getId());
        return mediumAClonar;
    }
    private Espiritu clonarEspirituConId(Espiritu espiritu){
        var espirituAClonar = new EspirituAngelical(espiritu.getNombre(),espiritu.getEnergia(), espiritu.getUbicacion());
        espirituAClonar.setId(espiritu.getId());
        return espirituAClonar;
    }


    @Test
    void testGetEspirituById() throws Throwable{
        mockMVCUbicacionController.guardarUbicacion(ubicacion1, HttpStatus.CREATED);

        mockMVCEspirituController.guardarEspiritu(espiritu1, HttpStatus.CREATED);
    }

    @Test
    void testALl() throws Throwable{
        mockMVCUbicacionController.guardarUbicacion(ubicacion1, HttpStatus.CREATED);
        mockMVCUbicacionController.guardarUbicacion(ubicacion2, HttpStatus.CREATED);

        mockMVCEspirituController.guardarEspiritu(espiritu1, HttpStatus.CREATED);
        mockMVCEspirituController.guardarEspiritu(espiritu, HttpStatus.CREATED);


        assertEquals(mockMVCEspirituController.recuperarTodos().size(), 2);
    }


    @Test
    void testGetAllEspiritus() throws Throwable{
        mockMVCEspirituController.guardarEspiritu(espiritu1, HttpStatus.CREATED);
        mockMVCEspirituController.guardarEspiritu(espiritu, HttpStatus.CREATED);

        Collection<Espiritu> espiritus = mockMVCEspirituController.recuperarTodos();

        assertEquals(2,espiritus.size());
    }

    @Test
    void testUnEspirituSePuedeConectarAUnMedium() throws Throwable{
        mockMVCEspirituController.guardarEspiritu(espiritu, HttpStatus.CREATED);


        mockMVCEspirituController.conectarAMedium(espiritu.getId(), medium.getId());
    }

    @Test
    void testunEspirituNoSePuedeConectarConUnMediumEnDistintaUbicacion() throws Throwable{
        mockMVCEspirituController.guardarEspiritu(espiritu1, HttpStatus.CREATED);

       ServletException thrown = assertThrows(ServletException.class, () -> {
            mockMVCEspirituController.conectarAMedium(espiritu1.getId(), medium.getId());
        });

        assertThrows(EspirituNoSeEncuentraEnLaMismaUbicacionException.class, () -> {
            throw thrown.getCause();
        });
    }

    @Test
    void testPaginacion() throws Throwable {

        EspirituDemoniaco demoniaco1 = new EspirituDemoniaco("Valak", 60, ubicacion1);
        EspirituDemoniaco demoniaco2 = new EspirituDemoniaco("Beelzebub", 90, ubicacion1);
        espirituService.guardar(demoniaco1);
        espirituService.guardar(demoniaco2);

        int cant = mockMVCEspirituController.getEspiritusDemoniacosPaginados(Direccion.ASCENDENTE,1,1).size();
        assertEquals(cant,1);
    }



    @AfterEach
    void clear(){
        mediumService.eliminarTodo();
        espirituService.eliminarTodo();
        ubicacionService.eliminarTodo();
    }

}
