package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.HabilidadDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dto.EspirituJPADTO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.modelo.Direccion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private EspirituDAO espirituDAO;
    private MediumDAO mediumDAO;
    private UbicacionDAO ubicacionDAO;
    private HabilidadDAO habilidadDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO, UbicacionDAO ubicacionDAO, HabilidadDAO habilidadDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
        this.ubicacionDAO = ubicacionDAO;
        this.habilidadDAO = habilidadDAO;
    }


    @Override
    public void guardar(Espiritu espiritu) {
        EspirituJPADTO espirituJPADTO = EspirituJPADTO.desdeModelo(espiritu);
        espirituDAO.save(espirituJPADTO);
        espiritu.setId(espirituJPADTO.getId());

    }

    @Override
    public void guardar(Espiritu espiritu, Long ubicacionId) {
        Ubicacion ubicacion = ubicacionDAO.findById(ubicacionId)
                .orElseThrow(() -> new NoSuchElementException("Ubicacion no encontrada con id: " + ubicacionId))
                .aModelo();
        espiritu.setUbicacion(ubicacion);
        EspirituJPADTO espirituJPADTO = EspirituJPADTO.desdeModelo(espiritu);
        espirituDAO.save(espirituJPADTO);
        espiritu.setId(espirituJPADTO.getId());
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return espirituDAO.recuperarEspiritu(espirituId).aModelo();
    }

    @Override
    public List<Espiritu> recuperarTodos() {

        return List.copyOf(espirituDAO.recuperarTodos())
                .stream()
                .map(EspirituJPADTO::aModelo)
                .toList();
    }

    @Override
    public void eliminar(Espiritu espiritu) {
        espiritu.eliminar();
        espirituDAO.save(EspirituJPADTO.desdeModelo(espiritu));
    }

    @Override
    public void eliminarTodo() { //Se mantiene para poder limpiar la base de datos en los tests
        espirituDAO.deleteAll();
    }

    @Override
    @Transactional
    public Medium conectar(Long espirituId, Long mediumId) {
            Espiritu espiritu = espirituDAO.recuperarEspiritu(espirituId).aModelo();

            Medium medium = mediumDAO.findById(mediumId).orElseThrow(() ->
                    new NoSuchElementException("Medium no encontrado con id:  " + mediumId))
                    .aModelo();

            espiritu.conectar(medium);


            espirituDAO.save(EspirituJPADTO.desdeModelo(espiritu));

            return medium;
}

    @Override
    public List<Espiritu> recuperarEspiritusDemoniacos() {

        return espirituDAO.recuperarEspiritusDemoniacos().stream()
                .map(EspirituJPADTO::aModelo)
                .toList();
    }

    @Override
    public List<Espiritu> recuperarEspiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina) {
        Sort sort = (direccion == Direccion.ASCENDENTE)
                ? Sort.by("energia").ascending()
                : Sort.by("energia").descending();

        Pageable pageable = PageRequest.of(pagina, cantidadPorPagina, sort);
        Page<EspirituJPADTO> paginaDeEspiritus = espirituDAO.recuperarEspiritusDemoniacos(pageable);

        // Lo paso a formato List
        return paginaDeEspiritus.getContent().stream()
                .map(EspirituJPADTO::aModelo)
                .toList();
    }
    @Override
    public void agregarHabilidadAEspiritu(Long espirituId, Long habilidadId) {
        // Busca el espíritu en la base de datos relacional
        Espiritu espiritu = espirituDAO.recuperarEspiritu(espirituId).aModelo();

        // Busca la habilidad en la base de datos relacional
        Habilidad habilidad = habilidadDAO.findById(habilidadId).orElseThrow(() ->
                        new NoSuchElementException("Habilidad no encontrado con id:  " + habilidadId))
                .aModelo();

        // Agrega la habilidad al espíritu
        espiritu.getHabilidades().add(habilidad);

        // Guarda el espíritu actualizado en la base de datos relacional
        espirituDAO.save(EspirituJPADTO.desdeModelo(espiritu));
    }


}
