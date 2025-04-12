package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.*;

public record EspirituRequestDTO(Long id, String nombre, Integer energia, EspirituTipo tipo, SimpleMediumDTO mediumConectado, Long ubicacionId) {

    public static EspirituRequestDTO desdeModelo(Espiritu espiritu) {
        return new EspirituRequestDTO(
                espiritu.getId(),
                espiritu.getNombre(),
                espiritu.getEnergia(),
                espiritu.getTipo(),
                espiritu.getMediumConectado() != null ? SimpleMediumDTO.desdeModelo(espiritu.getMediumConectado()) : null,
                espiritu.getUbicacion().getId()
        );
    }

    public Espiritu aModelo() {
        var ubicacion = new Cementerio(); // se pone por default al salir del aModelo se hace override de la misma.
        ubicacion.setId(this.ubicacionId);
        switch (tipo){
            case ANGELICAL -> {
                EspirituAngelical angelical = new EspirituAngelical(this.nombre, this.energia,ubicacion);
                angelical.setId(this.id);
                return angelical;
            }
            case DEMONIO -> {
                EspirituDemoniaco demoniaco = new EspirituDemoniaco(this.nombre, this.energia,ubicacion);
                demoniaco.setId(this.id);
                return demoniaco;
            }
            default -> throw new IllegalArgumentException("Argumentos no validos");
        }
    }

}