package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.UbicacionTipo;

public record UbicacionDTO(Long id, String nombre , int energia, UbicacionTipo tipo) {

    public static UbicacionDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTO(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia(),
                ubicacion.getTipo()
        );
    }

    public Ubicacion aModelo() {
        switch (tipo){
            case CEMENTERIO -> {
                Cementerio cementerio = new Cementerio(this.nombre, this.energia);
                cementerio.setId(this.id);
                return cementerio;
            }
            case SANTUARIO -> {
                Santuario santuario = new Santuario(this.nombre, this.energia);
                santuario.setId(this.id);
                return santuario;
            }
            default -> throw new IllegalArgumentException("Este no es un tipo valido de ubicacion");
        }
    }


}
