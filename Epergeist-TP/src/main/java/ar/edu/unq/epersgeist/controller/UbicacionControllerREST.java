package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.edu.unq.epersgeist.servicios.UbicacionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/ubicaciones")
public final class UbicacionControllerREST {

    private final UbicacionService ubicacionService;

    public UbicacionControllerREST(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public List<UbicacionDTO> getAllUbicaciones() {
        return ubicacionService.recuperarTodos().stream()
                .map(UbicacionDTO::desdeModelo)
                .collect(Collectors.toList());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteUbicacionById(@PathVariable Long id){
            Ubicacion ubicacion = ubicacionService.recuperar(id);
            ubicacionService.eliminar(ubicacion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ubicacióm eliminada con éxito");
        }

        @PostMapping()
        public ResponseEntity<String> createUbicacion(@RequestBody UbicacionDTO ubicacionDTO) {
            ubicacionService.guardar(ubicacionDTO.aModelo());

            return ResponseEntity.status(HttpStatus.CREATED).body("Ubicación creada con éxito");
        }

        @GetMapping("/{id}")
        public ResponseEntity<UbicacionDTO> getUbicacionById(@PathVariable Long id) {
            Ubicacion ubicacion = ubicacionService.recuperar(id);
            return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacion));
        }

        @GetMapping("/{id}/espiritus")
        public List<EspirituDTO> getEspiritusEnUbicacionById(@PathVariable Long id) {
            return ubicacionService.espiritusEn(id).stream()
                    .map(EspirituDTO::desdeModelo)
                    .collect(Collectors.toList());
        }

        @GetMapping("/{id}/mediumsSinEspiritus")
        public List<MediumDTO> getMediumsSinEspiritusEnUbicacionById(@PathVariable Long id) {
            return ubicacionService.mediumsSinEspiritusEn(id).stream()
                    .map(MediumDTO::desdeModelo)
                    .collect(Collectors.toList());
        }

        @GetMapping("/santuarioCorrupto")
        public ReporteSantuarioMasCorrupto getSantuarioMasCorrupto(){
            return ubicacionService.santuarioCorrupto();
        }

}
