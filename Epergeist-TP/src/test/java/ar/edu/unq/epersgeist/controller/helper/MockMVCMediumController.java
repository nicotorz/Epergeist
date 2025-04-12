package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
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
public class MockMVCMediumController{
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
    public void exorcizar(Long idMediumExorcista, Long idMediumExorcizado) throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders.put("/medium/" + idMediumExorcizado +"/exorcizar/" + idMediumExorcista))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public void guardarMedium(Medium medium, HttpStatus expectedStatus) throws Throwable {
        var dto = MediumRequestDTO.desdeModelo(medium);
        var json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/medium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    public Collection<Espiritu> allEspiritus(Long mediumId) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/medium/" + mediumId + "/espiritus"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<EspirituDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
        );

        return dtos.stream().map(EspirituDTO::aModelo).collect(Collectors.toList());
    }
}
