package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;


public record EspirituDTO(Long id, String nombre, Integer energia, EspirituTipo tipo, LocalDate fechaEliminado, SimpleMediumDTO mediumConectado, UbicacionDTO ubicacion, Set<HabilidadDTO> habilidades) {

    public static EspirituDTO desdeModelo(Espiritu espiritu) {
        return new EspirituDTO(
                espiritu.getId(),
                espiritu.getNombre(),
                espiritu.getEnergia(),
                espiritu.getTipo(),
                espiritu.getFechaEliminado(),
                espiritu.getMediumConectado() != null ? SimpleMediumDTO.desdeModelo(espiritu.getMediumConectado()) : null,
                UbicacionDTO.desdeModelo(espiritu.getUbicacion()),
                espiritu.getHabilidades().stream().map(HabilidadDTO::desdeModelo).collect(Collectors.toSet())

        );
    }


    public Espiritu aModelo() {
        switch (tipo){
            case ANGELICAL -> {
                EspirituAngelical angelical = new EspirituAngelical(this.nombre, this.energia,ubicacion.aModelo());
                angelical.setId(this.id);
                angelical.setHabilidades(this.habilidades.stream().map(HabilidadDTO::aModelo).collect(Collectors.toSet()));
                return angelical;
            }
            case DEMONIO -> {
                EspirituDemoniaco demoniaco = new EspirituDemoniaco(this.nombre, this.energia,ubicacion.aModelo());
                demoniaco.setId(this.id);
                demoniaco.setHabilidades(this.habilidades.stream().map(HabilidadDTO::aModelo).collect(Collectors.toSet()));
                return demoniaco;
            }
            default -> throw new IllegalArgumentException("Argumentos no validos");
        }
    }
}


