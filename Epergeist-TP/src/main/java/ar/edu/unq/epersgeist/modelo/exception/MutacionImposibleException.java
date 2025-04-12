package ar.edu.unq.epersgeist.modelo.exception;

import java.io.Serial;

public class MutacionImposibleException extends RuntimeException {

  public MutacionImposibleException(String habilidadOrigen ,String habilidadDestino) {
    this.habilidadOrigen = habilidadOrigen;
    this.habilidadDestino =habilidadDestino;
  }

  @Serial
  private static final long serialVersionUID = 1L;
  private final String habilidadOrigen;
  private final String habilidadDestino;


  @Override
  public String getMessage() {
    return "La habilidad" + habilidadOrigen+ "no esta conenctada con la habilidad" + habilidadDestino + "pero no con las evaluaciones dadas";
  }

}
