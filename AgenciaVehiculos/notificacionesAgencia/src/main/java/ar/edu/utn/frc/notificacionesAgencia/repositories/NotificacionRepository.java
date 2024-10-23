package ar.edu.utn.frc.notificacionesAgencia.repositories;

import ar.edu.utn.frc.notificacionesAgencia.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
