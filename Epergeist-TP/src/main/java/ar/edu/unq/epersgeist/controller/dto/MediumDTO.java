package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record MediumDTO(Long id, String nombre, Integer manaMax, Integer mana, UbicacionDTO ubicacion, Set<SimpleEspirituDTO> espiritus) {

    public static MediumDTO desdeModelo(Medium medium) {
        return new MediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                UbicacionDTO.desdeModelo(medium.getUbicacion()),
                medium.getEspiritus().stream()
                        .map(SimpleEspirituDTO::desdeModelo)
                        .collect(Collectors.toCollection(HashSet::new))
        );
    }
}
