package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public record MediumRequestDTO(Long id, String nombre, Integer manaMax, Integer mana, Long ubicacionId, Set<SimpleEspirituDTO> espiritus) {

    public static MediumRequestDTO desdeModelo(Medium medium) {
        return new MediumRequestDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion().getId(),
                medium.getEspiritus().isEmpty() ? null : medium.getEspiritus().stream().map(SimpleEspirituDTO::desdeModelo).collect(Collectors.toSet())
        );
    }
    public Medium aModelo() {
        Ubicacion ubicacion = new Santuario();
        ubicacion.setId(this.ubicacionId);
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, ubicacion);
        medium.setId(this.id);

        medium.setEspiritus(new HashSet<>());

        return medium;
    }
}
