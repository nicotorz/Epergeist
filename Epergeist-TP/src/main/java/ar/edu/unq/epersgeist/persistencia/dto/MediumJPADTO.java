package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@Entity(name = "Medium")
public class MediumJPADTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Positive
    private Integer manaMax;

    private Integer mana;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ubicacion_id")
    private UbicacionJPADTO ubicacion;

    @OneToMany(mappedBy="mediumConectado" ,cascade = CascadeType.ALL,  fetch = FetchType.EAGER )
    private Set<EspirituJPADTO> espiritus = new HashSet<>();


    public static MediumJPADTO desdeModelo(Medium medium) {
        MediumJPADTO dto = new MediumJPADTO();
        dto.nombre  =   medium.getNombre();
        dto.mana = medium.getMana();
        dto.manaMax    = medium.getManaMax();
        dto.id = medium.getId();
        dto.ubicacion = UbicacionJPADTO.desdeModelo(medium.getUbicacion());
        dto.espiritus = medium.getEspiritus().stream()
                .map(espiritu -> EspirituJPADTO.desdeModelo(espiritu,dto))
                .collect(Collectors.toSet());
        return dto;
    }

    public Medium aModelo() {

        Medium medium = new Medium(nombre,manaMax,mana,ubicacion.aModelo());
        medium.setEspiritus(espiritus.stream()
                .map(espiritus -> espiritus.aModelo(medium))
                .collect(Collectors.toSet()));
        medium.setId(this.id);
        return medium;
    }
}
