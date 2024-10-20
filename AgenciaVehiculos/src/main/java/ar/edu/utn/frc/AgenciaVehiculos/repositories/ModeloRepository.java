package ar.edu.utn.frc.AgenciaVehiculos.repositories;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
}
