package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCMediumController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class MediumControllerTest {

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

    private Ubicacion iglesia;
    private Ubicacion rio;

    private Espiritu espirituDemonio;
    private Espiritu espirituAngel;

    private Medium mediumFabri;
    private Medium mediumNico;
    @Autowired
    private MockMVCMediumController mockMVCMediumController;

    @BeforeEach
    void setUp(){
        iglesia = new Santuario("Iglesia santa", 40);
        rio = new Cementerio("Rio", 20);

        ubicacionService.guardar(iglesia);
        ubicacionService.guardar(rio);

        var ubicacion1 = this.clonarUbicacionConId(iglesia);
        var ubicacion2 = this.clonarUbicacionConId(rio);

        var fabri = new Medium("fabri", 100, 0, ubicacion1);
        var nico = new Medium("nico", 100, 20, ubicacion2);
        mediumService.guardar(fabri);
        mediumService.guardar(nico);

        mediumFabri = this.clonarMediumConId(fabri);
        mediumNico = this.clonarMediumConId(nico);

        var espiritu1 = new EspirituAngelical("Jeremias", 50, ubicacion1);
        var espiritu2 = new EspirituDemoniaco(  "Slenderman",50, ubicacion2);

        espiritu2.setNivelDeConexion(0);
        espiritu1.setNivelDeConexion(66);

        espirituService.guardar(espiritu1);
        espirituService.guardar(espiritu2);

        espirituAngel = this.clonarEspirituConId(espiritu1);
        espirituDemonio = this.clonarEspirituConId(espiritu2);

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
    void testUnMediumRealizaUnExorcismoExitoso() throws Throwable{
        Long idExorcista = mediumFabri.getId();
        Long idAExorcisar = mediumNico.getId();

        mockMVCMediumController.guardarMedium(mediumFabri, HttpStatus.CREATED);
        mockMVCMediumController.guardarMedium(mediumNico, HttpStatus.CREATED);

        mockMVCEspirituController.conectarAMedium(espirituAngel.getId(), idExorcista); //espiritu angelical
        mockMVCEspirituController.conectarAMedium(espirituDemonio.getId(), idAExorcisar); //espiritu demonio

        mockMVCMediumController.exorcizar(idExorcista, idAExorcisar);

        Collection<Espiritu> esp = mockMVCMediumController.allEspiritus(idAExorcisar);
        assertTrue(mockMVCMediumController.allEspiritus(idAExorcisar).isEmpty());

    }

    @AfterEach
    void tearDown(){
        espirituService.eliminarTodo();
        mediumService.eliminarTodo();
        ubicacionService.eliminarTodo();
    }
}
