package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


public class EspirituAngelicalTest {

    private EspirituAngelical angel;
    private EspirituDemoniaco demonio;
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;

    @BeforeEach
    void setUp() {

        ubicacion1 = new Cementerio("Quilmes", 50);
        ubicacion2 = new Santuario("Berazategui", 50);

        angel = new EspirituAngelical("Tyrael",100, ubicacion1);
        demonio = new EspirituDemoniaco( "Demonio1",60, ubicacion2);



    }

    @Test
    public void elEspirituAngelCambiaDeUbicacion() {

        assertEquals(angel.getUbicacion(), ubicacion1);
        angel.setUbicacion(ubicacion2);
        assertEquals(angel.getUbicacion(), ubicacion2);

    }
    @Test
    public void elEspirituAngelAtacaAEspirituDemoniaco() {
        EspirituAngelical espirituAngelicalSpy = spy(angel);
        //porcentaje exitoso
        doReturn(40).when(espirituAngelicalSpy).getNivelDeConexion();
        doReturn(70).when(espirituAngelicalSpy).calcularPorcentajeExito();

        espirituAngelicalSpy.atacar(demonio);

        assertEquals(40, demonio.getEnergia());
        assertEquals(espirituAngelicalSpy.getEnergia(), 90);
        assertEquals(espirituAngelicalSpy.getNivelDeConexion(),40);

    }






}
