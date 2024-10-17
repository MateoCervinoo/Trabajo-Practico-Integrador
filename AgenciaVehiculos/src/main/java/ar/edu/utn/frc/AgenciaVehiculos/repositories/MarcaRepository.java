package ar.edu.utn.frc.AgenciaVehiculos.repositories;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
}
