package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CondicionDTO;
import ar.edu.unq.epersgeist.controller.dto.EvaluacionDTO;
import ar.edu.unq.epersgeist.controller.dto.HabilidadDTO;
import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCHabilidadController;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dto.HabilidadJPADTO;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class HabilidadControllerTest {

    @Autowired
    private MockMVCHabilidadController mockMVCHabilidadController;

    @Autowired
    private HabilidadService habilidadService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private Habilidad habilidad;
    private Condicion condicion;


    private Habilidad habilidadTest;
    private CondicionDTO condicionTest;


    @BeforeEach
    void setUp() {

        var habilidad = new Habilidad("habilidad1");
        var habilidad2 = new Habilidad("habilidad2");

        var condicion = new Condicion(5, Evaluacion.EXORCISMOSEVITADOS);

        habilidadService.crear(habilidad);
        habilidadService.crear(habilidad2);
        //habilidadService.descubrirHabilidad(habilidad.getNombre(), habilidad2.getNombre(), condicion);

        //habilidadTest = habilidadService.recuperar(habilidad.getNombre());

        habilidadTest = this.clonarHabilidadConId(habilidad);


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

    private Habilidad clonarHabilidadConId(Habilidad habilidad){
        var habilidadAClonar = new Habilidad(habilidad.getNombre());
        habilidadAClonar.setId(habilidad.getId());
        return habilidadAClonar;
    }

    private Condicion clonarCondicionConId(Condicion condicion){
        var condicionAClonar = new Condicion(condicion.getCantidadNecesaria(),condicion.getEvaluacion());
        condicionAClonar.setId(condicion.getId());
        return condicionAClonar;
    }


    @Test
    void testCrearHabilidad() throws Throwable {
        mockMVCHabilidadController.crearHabilidad(habilidadTest, HttpStatus.CREATED);
    }


}
