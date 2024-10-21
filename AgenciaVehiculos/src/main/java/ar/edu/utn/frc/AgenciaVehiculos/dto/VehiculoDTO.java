package ar.edu.utn.frc.AgenciaVehiculos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehiculoDTO {
    private String patente;
    private ModeloDTO modelo;
    private int anio;
}
