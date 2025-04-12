package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dto.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.dto.UbicacionJPADTO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {
    private final UbicacionDAO ubicacionDAO;
    public UbicacionServiceImpl(UbicacionDAO ubicacionDAO) {
        this.ubicacionDAO = ubicacionDAO;
    }


    @Override
    public void guardar(Ubicacion ubicacion) { //Tambien actualiza
        UbicacionJPADTO ubicacionJPADTO = UbicacionJPADTO.desdeModelo(ubicacion);
        ubicacionDAO.save(ubicacionJPADTO);
        ubicacion.setId(ubicacionJPADTO.getId());
    }


    @Override
    public Ubicacion recuperar(Long ubicacionId) {
        return ubicacionDAO.findById(ubicacionId).orElseThrow(() ->
                new NoSuchElementException("Ubicacion no encontrada con id: " + ubicacionId))
                .aModelo();
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(ubicacionDAO.findAll())
                .stream()
                .map(UbicacionJPADTO::aModelo)
                .toList();
    }

    @Override
    public void eliminar(Ubicacion ubicacion) {
        ubicacionDAO.delete(UbicacionJPADTO.desdeModelo(ubicacion));
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return ubicacionDAO.espiritusEn(ubicacionId).stream()
                .map(EspirituJPADTO::aModelo)
                .toList();
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return ubicacionDAO.mediumsSinEspiritusEn(ubicacionId).stream()
                .map(MediumJPADTO::aModelo)
                .toList();
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {


        Santuario santuarioMasCorrupto = (Santuario) ubicacionDAO.obtenerSantuarioMasCorrupto().aModelo();

        long ubicacionId = santuarioMasCorrupto.getId();

        Medium mediumConMayorCantidadDemonios = ubicacionDAO.mediumConMayorCantidadDemoniosEn(ubicacionId).aModelo();

        int cantidadTotalDemonios = ubicacionDAO.cantidadTotalDemoniosEn(ubicacionId);

        int cantidadDemoniosLibres = ubicacionDAO.cantidadDemoniosLibresEn(ubicacionId);

        return new ReporteSantuarioMasCorrupto(
                santuarioMasCorrupto.getNombre(),
                mediumConMayorCantidadDemonios,
                cantidadTotalDemonios,
                cantidadDemoniosLibres
        );
    }

    @Override
    public void eliminarTodo() {
        ubicacionDAO.deleteAll();
    }





}
