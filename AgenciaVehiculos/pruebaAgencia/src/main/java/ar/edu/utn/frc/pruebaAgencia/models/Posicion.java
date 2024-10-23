package ar.edu.utn.frc.pruebaAgencia.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Posiciones")
@Data
@NoArgsConstructor
public class Posicion {
    @Id
    @GeneratedValue(generator = "posiciones")
    @TableGenerator(name = "posiciones", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue = "id",
            initialValue = 1, allocationSize = 1)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_VEHICULO", referencedColumnName = "ID")
    private Vehiculo vehiculo;

    @Column(name = "FECHA_HORA")
    private String fechaHora;

    @Column(name = "LATITUD")
    private int latitud;

    @Column(name = "LONGITUD")
    private int longitud;
}
