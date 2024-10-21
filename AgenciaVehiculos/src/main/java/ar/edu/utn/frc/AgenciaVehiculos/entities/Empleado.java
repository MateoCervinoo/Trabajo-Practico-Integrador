package ar.edu.utn.frc.AgenciaVehiculos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Empleados")
@Data
@NoArgsConstructor
public class Empleado {
    @Id
    @Column(name = "LEGAJO")
    private int legajo;

    @Column(name = "NOMBRE")
    private String nombreEmpleado;

    @Column(name = "APELLIDO")
    private String apellidoEmpleado;

    @Column(name = "TELEFONO_CONTACTO")
    private int telefonoContacto;

    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Prueba> pruebasEmpleado;
}
