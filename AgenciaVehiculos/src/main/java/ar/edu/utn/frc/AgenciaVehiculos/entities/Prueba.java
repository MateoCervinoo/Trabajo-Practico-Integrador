package ar.edu.utn.frc.AgenciaVehiculos.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Pruebas")
@Data
@NoArgsConstructor
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", referencedColumnName = "ID")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "ID_INTERESADO", referencedColumnName = "ID")
    private Interesado interesado;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "LEGAJO")
    private Empleado empleado;

    @Column(name = "FECHA_HORA_INICIO")
    private String fechaInicio;

    @Column(name = "FECHA_HORA_FIN")
    private String fechaFin;

    @Column(name = "COMENTARIOS")
    private String comentarios;
}
