package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dto.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.UbicacionJPADTO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    private final UbicacionDAO ubicacionDAO;

    public MediumServiceImpl(MediumDAO mediumDAO, EspirituDAO espirituDAO, UbicacionDAO ubicacionDAO) {
        this.mediumDAO = mediumDAO;
        this.espirituDAO = espirituDAO;
        this.ubicacionDAO = ubicacionDAO;
    }


    @Override
    public void guardar(Medium medium) {

        MediumJPADTO mediumJPADTO = MediumJPADTO.desdeModelo(medium);
        mediumDAO.save(mediumJPADTO);
        medium.setId(mediumJPADTO.getId());

    }

    @Override
    public void guardar(Medium medium, Long ubicacionId) {
        Ubicacion ubicacion = ubicacionDAO.findById(ubicacionId)
                .orElseThrow(() -> new NoSuchElementException("Ubicacion no encontrada con id: " + ubicacionId))
                .aModelo();
        medium.setUbicacion(ubicacion);
        MediumJPADTO mediumJPADTO = MediumJPADTO.desdeModelo(medium);
        mediumDAO.save(mediumJPADTO);
        medium.setId(mediumJPADTO.getId());
    }

    @Override
    public Medium recuperar(Long mediumID) {
        return mediumDAO.findById(mediumID).orElseThrow(() ->
                new NoSuchElementException("Medium no encontrado con id:  " + mediumID))
                .aModelo();
    }

    @Override
    public List<Medium> recuperarTodos() {
        return List.copyOf(mediumDAO.findAll()).stream()
                .map(MediumJPADTO::aModelo)
                .toList();
    }

    @Override
    public void eliminar(Medium medium) {
        MediumJPADTO mediumJPADTO = MediumJPADTO.desdeModelo(medium);
        mediumDAO.delete(mediumJPADTO);
    }

    @Override
    public void eliminarTodo() {
        mediumDAO.deleteAll();
    }

    @Override
    public void descansar(Long mediumId) {
        Medium mediumADescansar = mediumDAO.findById(mediumId).orElseThrow(() ->
                new NoSuchElementException("Medium no encontrado con id:  " + mediumId))
                .aModelo();

        mediumADescansar.descansar();
        MediumJPADTO mediumJPADTO = MediumJPADTO.desdeModelo(mediumADescansar);
        mediumDAO.save(mediumJPADTO);

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return mediumDAO.espiritusDe(mediumId).stream()
                .map(EspirituJPADTO::aModelo).
                toList();
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium mediumExorcista = mediumDAO.findById(idMediumExorcista).orElseThrow(() ->
                new NoSuchElementException("Medium no encontrado con id:  " + idMediumExorcista))
                .aModelo();

        Medium mediumAExorcizar = mediumDAO.findById(idMediumAExorcizar).orElseThrow(() ->
                new NoSuchElementException("Medium no encontrado con id:  " + idMediumAExorcizar))
                .aModelo();

        mediumExorcista.exorcizar(mediumAExorcizar);


        mediumDAO.save(MediumJPADTO.desdeModelo(mediumExorcista));
        mediumDAO.save(MediumJPADTO.desdeModelo(mediumAExorcizar));
    }

    @Override
    public Espiritu recuperarEspiritu(Long espirituID) {
        return espirituDAO.findById(espirituID).orElseThrow(() ->
                new NoSuchElementException("Espiritu no encontrado con id:  " + espirituID))
                .aModelo();
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        Espiritu espirituAInvocar = espirituDAO.findById(espirituId).orElseThrow(() ->
                new NoSuchElementException("Espiritu no encontrado con id:  " + espirituId))
                .aModelo();

        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() ->
                new NoSuchElementException("Medium no encontrado con id:  " + mediumId))
                .aModelo();

        medium.invocar(espirituAInvocar);


        mediumDAO.save(MediumJPADTO.desdeModelo(medium));

        return espirituAInvocar;
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() ->
                new NoSuchElementException("Medium no encontrado con id:  " + mediumId))
                .aModelo();
        Ubicacion ubicacion = ubicacionDAO.findById(ubicacionId).orElseThrow(() ->
                new NoSuchElementException("Ubicacion no encontrado con id:  " + ubicacionId))
                .aModelo();

        medium.moverse(ubicacion);

        mediumDAO.save(MediumJPADTO.desdeModelo(medium));
    }

}
