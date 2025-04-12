package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EspirituDemoniacoTest {

    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;

    @Mock
    private Medium medium;

    @BeforeEach
    public void setUp() {
        ubicacion1 = new Cementerio("Ubicacion", 50);
        ubicacion2 = new Santuario("Ubicacion", 50);

        espirituDemoniaco = new EspirituDemoniaco( "Demonio1",60, ubicacion1);
        espirituAngelical = new EspirituAngelical( "Tyrael",100, ubicacion2);

        medium = new Medium("medium",80, 100, ubicacion1);


    }

    @Test
    public void testRecibirAtaqueReduceEnergiaEnCasoDeSerAtaqueExitoso() {
        // Creo un "spy" del espirituAngelical para modificar su comportamiento
        EspirituAngelical espirituAngelicalSpy = spy(espirituAngelical);

        doReturn(40).when(espirituAngelicalSpy).getNivelDeConexion();
        doReturn(90).when(espirituAngelicalSpy).calcularPorcentajeExito();

        espirituAngelicalSpy.atacar(espirituDemoniaco);

        // Verifica que el ataque fue exitoso y que la energía del espíritu demoniaco se reduce en consecuencia
        // Tyrael tenia 40 de conexión, por lo que 40 / 2 = 20 puntos de efecto
        assertEquals(40, espirituDemoniaco.getEnergia());
        assertEquals(espirituAngelicalSpy.getEnergia(), 90);
        assertEquals(espirituAngelicalSpy.getNivelDeConexion(),40);
    }

    @Test
    public void testRecibirAtaqueReduceEnergiaEnCasoDeNoSerAtaqueExitoso() {

        EspirituAngelical espirituAngelicalSpy = spy(espirituAngelical);

        // No es exitoso el ataque
        doReturn(40).when(espirituAngelicalSpy).getNivelDeConexion();
        doReturn(10).when(espirituAngelicalSpy).calcularPorcentajeExito();

        espirituAngelicalSpy.atacar(espirituDemoniaco);

        assertEquals(60, espirituDemoniaco.getEnergia());
        assertEquals(espirituAngelicalSpy.getEnergia(), 90);
        assertEquals(espirituAngelicalSpy.getNivelDeConexion(),40);
    }


    @Test
    public void testRecibirAtaqueDesconectaMediumCuandoEnergiaEsCero() {
        espirituDemoniaco.setMediumConectado(medium);
        espirituDemoniaco.setEnergia(10);
        espirituAngelical.setNivelDeConexion(90);
        espirituAngelical.atacar(espirituDemoniaco);

        // Verifica que el Medium es null después de recibir el ataque que reduce la energía a 0
        assertNull(espirituDemoniaco.getMediumConectado());
    }

}
