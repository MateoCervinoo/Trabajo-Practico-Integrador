package ar.edu.utn.frc.AgenciaVehiculos.repositories;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Prueba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PruebaRepository extends JpaRepository<Prueba, Integer> {
}
