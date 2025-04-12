package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record SimpleMediumDTO(Long id, String nombre, Integer mana, UbicacionDTO ubicacion) {

    public static SimpleMediumDTO desdeModelo(Medium medium) {
        return new SimpleMediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getMana(),
                UbicacionDTO.desdeModelo(medium.getUbicacion())
        );
    }
}
