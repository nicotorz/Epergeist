package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.Habilidad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@Entity
public class HabilidadJPADTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;


    public HabilidadJPADTO() {}

    public HabilidadJPADTO(String nombre) {
        this.nombre = nombre;
    }

    public static Set<HabilidadJPADTO> desdeModelo(Set<Habilidad> habilidades) {
        return habilidades.stream().map(HabilidadJPADTO::desdeModelo).collect(Collectors.toSet());
    }

    public static HabilidadJPADTO desdeModelo(Habilidad habilidad) {
        HabilidadJPADTO dto = new HabilidadJPADTO();
        dto.nombre = habilidad.getNombre();
        dto.id = habilidad.getId();
        return dto;
    }

    public Habilidad aModelo() {
        Habilidad habilidad = new Habilidad(this.nombre);
        habilidad.setId(this.id);
        return habilidad;
    }


}
