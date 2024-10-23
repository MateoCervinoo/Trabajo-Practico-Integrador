package ar.edu.utn.frc.pruebaAgencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionDTO {
    private int vehiculoId;
    private String motivo;
    private String mensaje;
}
