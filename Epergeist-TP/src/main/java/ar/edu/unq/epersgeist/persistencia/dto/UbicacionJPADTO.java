package ar.edu.unq.epersgeist.persistencia.dto;

import ar.edu.unq.epersgeist.modelo.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
@Entity(name = "Ubicacion")
public class UbicacionJPADTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Min(0) @Max(100) // Limites de eneriga
    private int energia;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UbicacionTipo tipo;


    public static UbicacionJPADTO desdeModelo(Ubicacion ubicacion) {
        UbicacionJPADTO dto = new UbicacionJPADTO();
        dto.nombre  = ubicacion.getNombre();
        dto.energia = ubicacion.getEnergia();
        dto.tipo    = ubicacion.getTipo();
        dto.id = ubicacion.getId();
        return dto;
    }

    public Ubicacion aModelo() {

        Ubicacion ubicacion;
        if(tipo == UbicacionTipo.CEMENTERIO){
            ubicacion = new Cementerio(nombre,energia);
        }
        else{
            ubicacion = new Santuario(nombre, energia);
        }
        ubicacion.setId(this.id);
        return ubicacion;
    }

}
