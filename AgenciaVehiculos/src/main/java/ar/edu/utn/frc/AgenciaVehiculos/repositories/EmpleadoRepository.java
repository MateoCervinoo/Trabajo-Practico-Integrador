package ar.edu.utn.frc.AgenciaVehiculos.repositories;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
}
