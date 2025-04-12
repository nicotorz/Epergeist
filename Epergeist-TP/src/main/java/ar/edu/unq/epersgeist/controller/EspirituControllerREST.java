package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.MediumSinSuficienteManaException;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/espiritus")
final public class EspirituControllerREST {
    private final EspirituService espirituService;
    public EspirituControllerREST(EspirituService espirituService) {
        this.espirituService = espirituService;
    }

    @GetMapping
    public List<EspirituDTO> getAllEspiritus() {
        return espirituService.recuperarTodos().stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEspirituById(@PathVariable Long id){
        Espiritu espiritu = espirituService.recuperar(id);
        espirituService.eliminar(espiritu);
        return ResponseEntity.ok().body("Espiritu eliminado con exito");

    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTO> getEspirituById(@PathVariable Long id) {
        Espiritu espiritu = espirituService.recuperar(id);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }

    @PostMapping
    public ResponseEntity<String> createEspriritu(@RequestBody EspirituRequestDTO espriritu) {
        espirituService.guardar(espriritu.aModelo(), espriritu.ubicacionId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Espiritu creado con exito");
    }

    @PutMapping("/{idEspiritu}/conectar/{idMedium}")
    public ResponseEntity<MediumDTO> conectar(@PathVariable Long idEspiritu, @PathVariable Long idMedium){
        Medium medium = espirituService.conectar(idEspiritu, idMedium);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @GetMapping("/espiritusDemoniacos")
    public List<EspirituDTO> getEspiritusDemoniacos(){
        return espirituService.recuperarEspiritusDemoniacos().stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @GetMapping("/espiritusDemoniacos/{pagina}/{cantPorPagina}/{dir}")
    public List<EspirituDTO> getEspiritusDemoniacosPaginados(@PathVariable int pagina,@PathVariable int cantPorPagina,@PathVariable Direccion dir){
        return espirituService.recuperarEspiritusDemoniacos(dir,pagina,cantPorPagina).stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toList());
    }



}
