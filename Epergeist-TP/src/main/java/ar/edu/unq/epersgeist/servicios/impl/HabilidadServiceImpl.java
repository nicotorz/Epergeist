package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Condicion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Habilidad;
import ar.edu.unq.epersgeist.modelo.Evaluacion;
import ar.edu.unq.epersgeist.modelo.exception.HabilidadesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.exception.MutacionImposibleException;
import ar.edu.unq.epersgeist.persistencia.dto.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.HabilidadNeo;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.HabilidadDAO;
import ar.edu.unq.epersgeist.persistencia.dao.HabilidadNeoDAO;
import ar.edu.unq.epersgeist.persistencia.dto.HabilidadJPADTO;
import ar.edu.unq.epersgeist.servicios.HabilidadService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXResult;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HabilidadServiceImpl implements HabilidadService {
    private final HabilidadNeoDAO habilidadNeoDAO;
    private final EspirituDAO espirituDAO;
    private final HabilidadDAO habilidadDAO;

    public HabilidadServiceImpl(HabilidadNeoDAO habilidadNEODAO, EspirituDAO espirituDAO, HabilidadDAO habilidadDAO) {
        this.habilidadNeoDAO = habilidadNEODAO;
        this.espirituDAO = espirituDAO;
        this.habilidadDAO = habilidadDAO;
    }
    @Override
    public Habilidad crear(Habilidad habilidad) {

        HabilidadJPADTO habilidadJPATO = HabilidadJPADTO.desdeModelo(habilidad);
        habilidadDAO.save(habilidadJPATO);

        habilidad.setId(habilidadJPATO.getId());

        HabilidadNeo habilidadNeo = HabilidadNeo.desdeModelo(habilidad);

        habilidadNeoDAO.save(habilidadNeo);

        return habilidad;
    }

    @Override
    public void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, Condicion condicion) {
        habilidadNeoDAO.descubrirHabilidad(nombreHabilidadOrigen,
                                            nombreHabilidadDestino,
                                            condicion.getCantidadNecesaria(),
                                            condicion.getEvaluacion());
    }

    @Override
    public Habilidad recuperar(String nombreHabilidad) {
        HabilidadNeo habilidad = habilidadNeoDAO.findByNombre(nombreHabilidad).orElseThrow(()->
                        new NoSuchElementException("Habilidad no encontrada con el nombre: " + nombreHabilidad));
        return habilidad.aModelo();
    }

    @Override
    public void evolucionar(Long espirituId) {

        Espiritu espiritu = espirituDAO.recuperarEspiritu(espirituId).aModelo();
        Set<String> habilidad = espiritu.nombreHablidades();

        Set<String> nombresHabilidadesPosiblesAUnPaso = habilidadNeoDAO.habilidadesPosiblesAUnPaso(habilidad,
                                                                 espiritu.getEnergia(),
                                                                 espiritu.getNivelDeConexion(),
                                                                 espiritu.getExorsismosEvitados(),
                                                                 espiritu.getExorsismosResueltos());

        Set<HabilidadJPADTO> habilidadesJPADTO = habilidadDAO.recuperarPorNombres(nombresHabilidadesPosiblesAUnPaso);

        espiritu.mutarAHabilidad(habilidadesJPADTO.stream()
                                .map(HabilidadJPADTO::aModelo)
                                .collect(Collectors.toSet()));

        espirituDAO.save(EspirituJPADTO.desdeModelo(espiritu));
    }

    @Override
    public Set<Habilidad> habilidadesConectadas(String nombreHabilidad) {
        Set<HabilidadNeo> habilidadesNeos = habilidadNeoDAO.habilidadesConectadas(nombreHabilidad);
        return habilidadesNeos.stream()
                .map(HabilidadNeo::aModelo)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Habilidad> caminoMasRentable(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<Evaluacion> evaluaciones) {
        List<HabilidadNeo> habilidadNeo = habilidadNeoDAO.caminoMasRentable(nombreHabilidadOrigen,nombreHabilidadDestino,evaluaciones);

        if(habilidadNeo.isEmpty()) {
            if(habilidadNeoDAO.estanConectados(nombreHabilidadOrigen,nombreHabilidadDestino)){
                throw new MutacionImposibleException(nombreHabilidadOrigen,nombreHabilidadDestino);
            }
            throw new HabilidadesNoConectadasException(nombreHabilidadOrigen,nombreHabilidadDestino);
        }

        return habilidadNeo.stream()
                .map(HabilidadNeo::aModelo)
                .toList();
    }

    @Override
    public Set<Habilidad> habilidadesPosibles(Long espirituId) {
        Espiritu espiritu = espirituDAO.recuperarEspiritu(espirituId).aModelo();
        Set<String> nombreHablidades = espiritu.nombreHablidades();

        Set<HabilidadNeo> habilidades = habilidadNeoDAO.habilidadesPosibles(espiritu.getEnergia(),
                                                    espiritu.getNivelDeConexion(),
                                                    espiritu.getExorsismosEvitados(),
                                                    espiritu.getExorsismosResueltos(),
                                                    nombreHablidades);
        return habilidades.stream()
                .map(HabilidadNeo::aModelo)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Habilidad> caminoMasMutable(Long espirituId, String nombreHabilidad) {
        Espiritu espiritu = espirituDAO.recuperarEspiritu(espirituId).aModelo();

        List<HabilidadNeo> habilidades = habilidadNeoDAO.caminoMasMutable(nombreHabilidad,
                                                    espiritu.getEnergia(),
                                                    espiritu.getNivelDeConexion(),
                                                    espiritu.getExorsismosEvitados(),
                                                    espiritu.getExorsismosResueltos());
        return habilidades.stream()
                .map(HabilidadNeo::aModelo)
                .toList();

    }

    @Override
    public List<Habilidad> caminoMenosMutable(String nombreHabilidad, Long espirituId) {

        Espiritu espiritu = espirituDAO.recuperarEspiritu(espirituId).aModelo();
        List<HabilidadNeo> habilidades = habilidadNeoDAO.caminoMenosMutable(nombreHabilidad,
                                                    espiritu.getEnergia(),
                                                    espiritu.getNivelDeConexion(),
                                                    espiritu.getExorsismosEvitados(),
                                                    espiritu.getExorsismosResueltos());
        return habilidades.stream()
                .map(HabilidadNeo::aModelo)
                .toList();
    }

    @Override
    public void clearAll() {
        habilidadNeoDAO.detachDelete();
        habilidadDAO.deleteAll();
    }

    @Override
    public void eliminarTodo() { //Se mantiene para poder limpiar la base de datos en los tests
        habilidadDAO.deleteAll();
    }
}
