package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity(name = "Espiritu")
public class EspirituJPADTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Min(1) @Max(100)
    private Integer nivelDeConexion;

    private String nombre;

    @Min(0) @Max(100)
    protected Integer energia;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EspirituTipo tipo;

    private LocalDate fechaEliminado;

    private int exorcismoInvolucrado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ubicacion_id")
    protected UbicacionJPADTO ubicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium_id",nullable = true)
    private MediumJPADTO mediumConectado;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinTable(name = "espiritu_habilidad" ) // Nombre de la tabla intermedia
    private Set<HabilidadJPADTO> habilidad;



    private static EspirituJPADTO desdeModeloBasico(Espiritu espiritu) {
        EspirituJPADTO dto = new EspirituJPADTO();
        dto.nombre  = espiritu.getNombre();
        dto.energia = espiritu.getEnergia();
        dto.nivelDeConexion = espiritu.getNivelDeConexion();
        dto.tipo    = espiritu.getTipo();
        dto.fechaEliminado = espiritu.getFechaEliminado();
        dto.id = espiritu.getId();
        dto.ubicacion =  UbicacionJPADTO.desdeModelo(espiritu.getUbicacion());
        dto.habilidad = HabilidadJPADTO.desdeModelo(espiritu.getHabilidades());
        dto.exorcismoInvolucrado = espiritu.getExorcismosInvolucrados();
        return dto;
    }


    public static EspirituJPADTO desdeModelo(Espiritu espiritu) {
        EspirituJPADTO dto = desdeModeloBasico(espiritu);
        if (espiritu.getMediumConectado() != null) {
            dto.mediumConectado = MediumJPADTO.desdeModelo(espiritu.getMediumConectado());
        }
        return dto;
    }

    public static EspirituJPADTO desdeModelo(Espiritu espiritu, MediumJPADTO mediumConectadoJPADTO) {
        EspirituJPADTO dto = desdeModeloBasico(espiritu);
        if(dto.fechaEliminado == null) {
            dto.mediumConectado = mediumConectadoJPADTO;
        }
        return dto;
    }

    public Espiritu aModelo() {

        Espiritu espiritu;
        if(tipo == EspirituTipo.ANGELICAL){
            espiritu = new EspirituAngelical(nombre,energia,ubicacion.aModelo());
        }
        else{
            espiritu = new EspirituDemoniaco(nombre,energia,ubicacion.aModelo());
        }
        if(mediumConectado != null){
            espiritu.setMediumConectado(mediumConectado.aModelo());
        }
        espiritu.setNivelDeConexion(nivelDeConexion);
        espiritu.setFechaEliminado(fechaEliminado);
        espiritu.setId(id);
        espiritu.setExorcismosInvolucrados(exorcismoInvolucrado);
        espiritu.setHabilidades(habilidad.stream()
                .map(HabilidadJPADTO::aModelo)
                .collect(Collectors.toSet()));
        return espiritu;
    }

    public Espiritu aModelo(Medium medium) {

        Espiritu espiritu;
        if(tipo == EspirituTipo.ANGELICAL){
            espiritu = new EspirituAngelical(nombre,energia,ubicacion.aModelo());
        }
        else{
            espiritu = new EspirituDemoniaco(nombre,energia,ubicacion.aModelo());
        }
        espiritu.setHabilidades(habilidad.stream()
                .map(HabilidadJPADTO::aModelo)
                .collect(Collectors.toSet()));
        espiritu.setMediumConectado(medium);
        espiritu.setNivelDeConexion(nivelDeConexion);
        espiritu.setFechaEliminado(fechaEliminado);
        espiritu.setId(id);
        espiritu.setExorcismosInvolucrados(exorcismoInvolucrado);
        return espiritu;
    }

}
