package ar.edu.utn.frc.AgenciaVehiculos.repositories;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
}
