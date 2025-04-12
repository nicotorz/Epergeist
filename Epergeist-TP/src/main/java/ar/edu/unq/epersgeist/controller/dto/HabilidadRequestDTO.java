package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Habilidad;

import java.util.Set;
import java.util.stream.Collectors;

public record HabilidadRequestDTO(Long id, String nombre, Set<SimpleCondicionDTO> condiciones) {

    public static HabilidadRequestDTO desdeModelo(Habilidad habilidad) {
        return new HabilidadRequestDTO(
                habilidad.getId(),
                habilidad.getNombre(),
                habilidad.getCondicion().isEmpty() ? null : habilidad.getCondicion().stream()
                        .map(SimpleCondicionDTO::desdeModelo)
                        .collect(Collectors.toSet())
        );
    }

    public Habilidad aModelo() {
        Habilidad habilidad = new Habilidad(this.nombre);
        habilidad.setId(this.id);

        if (this.condiciones != null) {
            Set<Condicion> condiciones = this.condiciones.stream()
                    .map(SimpleCondicionDTO::aModelo)
                    .collect(Collectors.toSet());
            habilidad.setCondicion(condiciones);
        }

        return habilidad;
    }
}
