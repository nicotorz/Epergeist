package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumRequestDTO;
import ar.edu.unq.epersgeist.controller.dto.SimpleEspirituDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/medium")
final public class MediumControllerREST {
    private final MediumService mediumService;
    public MediumControllerREST(MediumService mediumService, UbicacionService ubicacionService) {
        this.mediumService = mediumService;
    }

    @GetMapping
    public List<MediumDTO> getAllMediums() {
        return mediumService.recuperarTodos().stream()
                .map(MediumDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> getMediumById(@PathVariable Long id) {
        Medium medium = mediumService.recuperar(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @PostMapping
    public ResponseEntity<String> createMedium(@RequestBody MediumRequestDTO medium) {
        mediumService.guardar(medium.aModelo(), medium.ubicacionId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Medium creado con éxito");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedium(@PathVariable Long id) {
        Medium mediumAEliminar = mediumService.recuperar(id);
        mediumService.eliminar(mediumAEliminar);
        return ResponseEntity.status(HttpStatus.OK).body("Medium eliminado con éxito");
    }

    @PutMapping("/{id}/descansar")
    public ResponseEntity<MediumDTO> descansarMedium(@PathVariable Long id) {
        mediumService.descansar(id);
        Medium medium = mediumService.recuperar(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @GetMapping("/{id}/espiritus")
    public List<SimpleEspirituDTO> getEspiritus(@PathVariable Long id) {
        return mediumService.espiritus(id).stream()
                .map(SimpleEspirituDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @PutMapping("/{idExorcista}/exorcizar/{idExorcizado}")
    public ResponseEntity<MediumDTO> exorcizarMedium(@PathVariable Long idExorcista, @PathVariable Long idExorcizado) {
        mediumService.exorcizar(idExorcizado, idExorcista);
        Medium mediumExorcizado = mediumService.recuperar(idExorcizado);
        return ResponseEntity.ok(MediumDTO.desdeModelo(mediumExorcizado));
    }

    @PutMapping("/{id}/invocar/{espirituId}")
    public ResponseEntity<EspirituDTO> invocarMedium(@PathVariable Long id, @PathVariable Long espirituId) {
        Espiritu espiritu = mediumService.invocar(id,espirituId);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }

    @PutMapping("{id}/mover/{ubicacionId}")
    public ResponseEntity<MediumDTO> moverMedium(@PathVariable Long id, @PathVariable Long ubicacionId) {
        mediumService.mover(id,ubicacionId);
        Medium medium = mediumService.recuperar(id);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }
}