package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.*;

public record SimpleEspirituDTO(String nombre, Long id, EspirituTipo tipo, Integer energia, UbicacionDTO ubicacion) {

    public static SimpleEspirituDTO desdeModelo(Espiritu espiritu) {
        return new SimpleEspirituDTO(
                espiritu.getNombre(),
                espiritu.getId(),
                espiritu.getTipo(),
                espiritu.getEnergia(),
                UbicacionDTO.desdeModelo(espiritu.getUbicacion())
        );
    }

    public Espiritu aModelo() {
        switch (tipo){
            case ANGELICAL -> {
                EspirituAngelical angelical = new EspirituAngelical(this.nombre, this.energia, this.ubicacion.aModelo());
                angelical.setId(this.id);
                return angelical;
            }
            case DEMONIO -> {
                EspirituDemoniaco demoniaco = new EspirituDemoniaco(this.nombre, this.energia, this.ubicacion.aModelo());
                demoniaco.setId(this.id);
                return demoniaco;
            }
            default -> throw new IllegalArgumentException("Argumentos no validos");
        }
    }
}
