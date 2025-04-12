package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.Habilidad;
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

import java.util.List;
import java.util.Set;

@Component
public class MockMVCHabilidadController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Throwable {
        try {
            return mockMvc.perform(requestBuilder);
        } catch (ServletException e) {
            throw e.getCause();
        }
    }

    public void crearHabilidad(Habilidad habilidad, HttpStatus expectedStatus) throws Throwable {
        var dto = HabilidadRequestDTO.desdeModelo(habilidad);
        var json = objectMapper.writeValueAsString(dto);

        performRequest(MockMvcRequestBuilders.post("/habilidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    public void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, CondicionDTO condicion) throws Throwable {
        var json = objectMapper.writeValueAsString(condicion);

        performRequest(MockMvcRequestBuilders.put("/habilidades/" + nombreHabilidadOrigen + "/descubrirHabilidad/" + nombreHabilidadDestino)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }


    public Set<HabilidadDTO> habilidadesConectadas(String nombreHabilidad) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/habilidades/habilidadesConectadas/" + nombreHabilidad))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(Set.class, HabilidadDTO.class)
        );
    }


    public List<HabilidadDTO> caminosMasRentables(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<EvaluacionDTO> evaluaciones) throws Throwable {
        var jsonRequest = objectMapper.writeValueAsString(evaluaciones);

        var json = performRequest(MockMvcRequestBuilders.get("/habilidades/" + nombreHabilidadOrigen + "/caminoMasRentable" + nombreHabilidadDestino)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, HabilidadDTO.class)
        );
    }










}

