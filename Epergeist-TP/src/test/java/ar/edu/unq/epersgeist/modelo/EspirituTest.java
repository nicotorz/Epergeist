package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.EspirituNoSeEncuentraEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituOcupadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituTest {

    private Espiritu espiritu;
    private Medium medium;
    private Ubicacion ubicacion;

    @BeforeEach
    public void setUp() {
        ubicacion = new Santuario("Ubicacion", 50);
        espiritu = new EspirituAngelical( "Jeremias", 10, ubicacion);
        medium = new Medium("Persona", 10, 10, ubicacion);
    }

    @Test
    void cuandoUnEspirituEsRecienCreadoSuIdEsNull(){

        assertNull(espiritu.getId());
    }

    @Test
    void unEspirituRecienCreadoTieneUnNombreAsignado(){

        assertEquals("Jeremias", espiritu.getNombre());
    }


    @Test
    void unEspirituRecienCreadoSuNivelDeConexionPorDefaultEsCero(){

        assertEquals(0, espiritu.getNivelDeConexion());
    }

    @Test
    void unEspirituGeneraUnaConexionConUnMediumSuNivelDeConexionAumenta(){

        espiritu.aumentarConexion(medium);

        assertEquals(2, espiritu.getNivelDeConexion());
    }

    @Test
    void cuandoUnEspirituQuiereAumentarSuConexionSinEstarEnLaMismaUbicacionLanzaUnaExcepcion(){
        Ubicacion ubicacion2 =new Santuario("el palacio", 50);
        espiritu.setUbicacion(ubicacion2);

        assertThrows(EspirituNoSeEncuentraEnLaMismaUbicacionException.class, () -> {
            espiritu.conectar(medium);
        });
    }

    @Test
    void cuandoUnEspirituQuiereAumentarSuConexionSiSeEncuentraOcupadoSeLanzaUnaExcepcion(){
        medium.conectarseAEspiritu(espiritu);

        assertThrows(EspirituOcupadoException.class, () -> {
            espiritu.conectar(medium);
        });
    }


}
