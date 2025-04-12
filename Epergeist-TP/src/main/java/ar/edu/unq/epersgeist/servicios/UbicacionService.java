package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;

public interface UbicacionService {

    void guardar(Ubicacion ubicacion);
    Ubicacion recuperar(Long espirituId);
    List<Ubicacion> recuperarTodos();
    void eliminar(Ubicacion ubicacion);
    void eliminarTodo();
    List<Espiritu> espiritusEn(Long ubicacionId); //Retorna los espíritus existentes en la ubicación dada.
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);

    ReporteSantuarioMasCorrupto santuarioCorrupto();

}
