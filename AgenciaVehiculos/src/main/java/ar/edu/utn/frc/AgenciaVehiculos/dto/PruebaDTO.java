package ar.edu.utn.frc.AgenciaVehiculos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PruebaDTO {
    private int id;
    private LocalDateTime fechaFin;
    private InteresadoDTO interesado;
    private VehiculoDTO vehiculo;
}
