package ar.edu.unq.epersgeist.modelo;


import ar.edu.unq.epersgeist.controller.dto.SimpleMediumDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReporteSantuarioMasCorrupto {

    private String nombreSantuario;
    private SimpleMediumDTO mediumConMasDemonios;
    private int totalDemonios;
    private int demoniosLibres;

    public ReporteSantuarioMasCorrupto(String nombreSantuario, Medium mediumConMasDemonios, int totalDemonios, int demoniosLibres) {
        this.nombreSantuario = nombreSantuario;
        this.mediumConMasDemonios = mediumConMasDemonios != null ? SimpleMediumDTO.desdeModelo(mediumConMasDemonios) : null;
        this.totalDemonios = totalDemonios;
        this.demoniosLibres = demoniosLibres;
    }

}
