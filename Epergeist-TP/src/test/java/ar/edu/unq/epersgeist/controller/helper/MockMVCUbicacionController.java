package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MockMVCUbicacionController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // MockMVC por default re-throwea "NestedServletException" cuando le llega un error.
    // Aca estamos esperando por esa excepcion, y re-throweamos la causa.
    // De esa forma, podemos testear tranquilos y esperar las excepciones de negocio y applicacion del otro lado.
    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Throwable {
        try {
            return mockMvc.perform(requestBuilder);
        } catch (ServletException e) {
            throw e.getCause();
        }
    }

    public void guardarUbicacion(Ubicacion espiritu, HttpStatus expectedStatus) throws Throwable {
        var dto = UbicacionDTO.desdeModelo(espiritu);
        var json = objectMapper.writeValueAsString(dto);


        mockMvc.perform(MockMvcRequestBuilders.post("/ubicaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value())
        );

    }


    public Ubicacion recuperarEspiritu(Long ubicacionId) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicaciones/" + ubicacionId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(json, UbicacionDTO.class);
        return dto.aModelo();
    }

    public Collection<Ubicacion> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicaciones"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UbicacionDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UbicacionDTO.class)
        );

        return dtos.stream().map(UbicacionDTO::aModelo).collect(Collectors.toList());
    }
}
