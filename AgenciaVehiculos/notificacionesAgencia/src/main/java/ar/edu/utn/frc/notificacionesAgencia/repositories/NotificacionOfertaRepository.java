package ar.edu.utn.frc.notificacionesAgencia.repositories;

import ar.edu.utn.frc.notificacionesAgencia.models.NotificacionOferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionOfertaRepository extends JpaRepository<NotificacionOferta, Integer> {
}
