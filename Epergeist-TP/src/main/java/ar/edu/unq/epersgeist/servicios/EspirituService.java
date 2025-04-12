package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Direccion;

import java.util.List;

public interface EspirituService {
    void guardar(Espiritu espiritu);
    void guardar(Espiritu espiritu, Long ubicacionId);
    Espiritu recuperar(Long espirituId);
    List<Espiritu> recuperarTodos();
    void eliminar(Espiritu espiritu);
    void eliminarTodo();

    Medium conectar(Long espirituId, Long mediumId);

    List<Espiritu> recuperarEspiritusDemoniacos();

    List<Espiritu> recuperarEspiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina);

    void agregarHabilidadAEspiritu(Long espirituId, Long habilidadId);
}
