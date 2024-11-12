package ar.edu.utn.frc.pruebaAgencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KilometrajeRecorridoDTO {
    private VehiculoDTO vehiculoDTO;
    private double kilometrosRecorridos;
}
