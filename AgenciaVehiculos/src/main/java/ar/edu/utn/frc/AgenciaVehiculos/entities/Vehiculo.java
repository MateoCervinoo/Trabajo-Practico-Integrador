package ar.edu.utn.frc.AgenciaVehiculos.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Vehiculos")
@Data
@NoArgsConstructor
public class Vehiculo {
    @Id
    @GeneratedValue(generator = "vehiculos")
    @TableGenerator(name = "vehiculos", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue = "ID",
            initialValue = 1, allocationSize = 1)
    private int id;

    @Column(name = "PATENTE")
    private String patente;

    @ManyToOne
    @JoinColumn(name = "ID_MODELO", referencedColumnName = "id")
    private Modelo modelo;

    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY)
    private List<Prueba> pruebasVehiculo;
}