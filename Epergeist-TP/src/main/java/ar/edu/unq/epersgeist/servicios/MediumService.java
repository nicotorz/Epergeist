package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;

public interface MediumService {
    void guardar(Medium medium);

    void guardar(Medium medium, Long ubicacionId);

    Medium recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void eliminar(Medium medium);
    void eliminarTodo();

    void descansar(Long mediumId);
    List<Espiritu> espiritus(Long mediumId);
    void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar);
    Espiritu recuperarEspiritu(Long espirituID);

    Espiritu invocar(Long mediumId, Long espirituId);
    void mover(Long mediumId, Long ubicacionId);
}
