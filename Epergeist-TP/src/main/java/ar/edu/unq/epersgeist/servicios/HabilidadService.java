package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.Evaluacion;

import java.util.List;
import java.util.Set;

public interface HabilidadService {
    Habilidad crear(Habilidad habilidad);
    void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, Condicion condicion);
    Habilidad recuperar(String nombreHabilidad);

    void evolucionar(Long espirituId);
    Set<Habilidad> habilidadesConectadas(String nombreHabilidad);
    List<Habilidad> caminoMasRentable(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<Evaluacion> evaluaciones);
    Set<Habilidad> habilidadesPosibles(Long espirituId);
    List<Habilidad> caminoMasMutable(Long espirituId, String nombreHabilidad);
    List<Habilidad> caminoMenosMutable(String nombreHabilidad, Long espirituId);
    void clearAll();

    void eliminarTodo();
}
