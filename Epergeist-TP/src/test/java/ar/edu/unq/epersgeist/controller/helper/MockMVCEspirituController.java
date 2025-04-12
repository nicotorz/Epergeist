package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituRequestDTO;
import ar.edu.unq.epersgeist.controller.dto.SimpleEspirituDTO;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
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
public class MockMVCEspirituController {
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

    public void guardarEspiritu(Espiritu espiritu, HttpStatus expectedStatus) throws Throwable {
        var dto = EspirituRequestDTO.desdeModelo(espiritu);
        var json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/espiritus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    public Collection<Espiritu> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/espiritus"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<EspirituDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
        );

        return dtos.stream().map(EspirituDTO::aModelo).collect(Collectors.toList());
    }

    public void conectarAMedium(Long espirituID, Long mediumId) throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders.put("/espiritus/" + espirituID +"/conectar/" + mediumId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public Collection<Espiritu> getEspiritusDemoniacosPaginados(Direccion dir, int pagina, int cantPorPagina) throws Throwable {
        var json = mockMvc.perform(
                        MockMvcRequestBuilders.get("/espiritus/espiritusDemoniacos/" + pagina + "/" + cantPorPagina + "/" + dir.name())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(); // Obtener el contenido de la respuesta

        Collection<EspirituDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
        );

        return dtos.stream().map(EspirituDTO::aModelo).collect(Collectors.toList());
    }
}
