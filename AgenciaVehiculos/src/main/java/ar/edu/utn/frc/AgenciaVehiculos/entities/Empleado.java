package ar.edu.utn.frc.AgenciaVehiculos.entities;

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
    @GeneratedValue(generator = "empleados")
    @TableGenerator(name = "empleados", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue = "LEGAJO",
            initialValue = 1, allocationSize = 1)
    private int id;

    @Column(name = "NOMBRE")
    private String nombreEmpleado;

    @Column(name = "APELLIDO")
    private String apellidoEmpleado;

    @Column(name = "TELEFONO_CONTACTO")
    private int telefonoContacto;

    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    private List<Prueba> pruebasEmpleado;
}
