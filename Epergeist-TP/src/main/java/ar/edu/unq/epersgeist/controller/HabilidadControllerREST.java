package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CondicionDTO;
import ar.edu.unq.epersgeist.controller.dto.EvaluacionDTO;
import ar.edu.unq.epersgeist.controller.dto.HabilidadDTO;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/habilidades")
public class HabilidadControllerREST {

    private final HabilidadService habilidadService;

    public HabilidadControllerREST(HabilidadService habilidadService){
        this.habilidadService = habilidadService;
    }

    @PostMapping()
    public ResponseEntity<String> createHabilidad(@RequestBody HabilidadDTO habilidadDTO){
        habilidadService.crear(habilidadDTO.aModelo());
        return ResponseEntity.status(HttpStatus.CREATED).body("Habilidad creada con éxito");
    }

    @PutMapping("{nombreHabilidadOrigen}/descubrirHabilidad/{nombreHabilidadDestino}")
    public ResponseEntity<String> descubrirHabilidad(@PathVariable String nombreHabilidadDestino, @PathVariable String nombreHabilidadOrigen,@RequestBody CondicionDTO condicion){
        habilidadService.descubrirHabilidad(nombreHabilidadOrigen, nombreHabilidadDestino, condicion.aModelo());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Habilidad descubrida con éxito");
    }

//    @PutMapping("/evolucionar/{espirituId}")
//    public ResponseEntity<String> evolucionar(@PathVariable Long espirituId){
//        habilidadService.evolucionar(espirituId);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Evolucion realizada con éxito");
//    }
//
    @GetMapping("/habilidadesConectadas/{nombreHabilidad}")
    public Set<HabilidadDTO> habilidadesConectadas(@PathVariable String nombreHabilidad){
        return habilidadService.habilidadesConectadas(nombreHabilidad).stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/habilidadesPosibles/{espirituId}")
    public Set<HabilidadDTO> habilidadesPosibles(@PathVariable Long espirituId){
        return habilidadService.habilidadesPosibles(espirituId).stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{nombreHabilidadOrigen}/caminoMasRentable/{nombreHabilidadDestino}")
    public List<HabilidadDTO> caminosMasRentables(@PathVariable String nombreHabilidadOrigen, @PathVariable String nombreHabilidadDestino, @RequestBody Set<EvaluacionDTO> evaluacion){
        return habilidadService.caminoMasRentable(nombreHabilidadOrigen,
                        nombreHabilidadDestino, EvaluacionDTO.aModelo(evaluacion)).stream()
                        .map(HabilidadDTO::desdeModelo)
                       .collect(Collectors.toList());
    }
}


