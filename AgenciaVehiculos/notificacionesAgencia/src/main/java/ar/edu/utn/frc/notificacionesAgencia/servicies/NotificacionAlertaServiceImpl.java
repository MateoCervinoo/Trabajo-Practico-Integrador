package ar.edu.utn.frc.notificacionesAgencia.servicies;

import ar.edu.utn.frc.notificacionesAgencia.dtos.NotificacionAlertaDTO;
import ar.edu.utn.frc.notificacionesAgencia.models.NotificacionAlerta;
import ar.edu.utn.frc.notificacionesAgencia.repositories.NotificacionAlertaRepository;
import ar.edu.utn.frc.notificacionesAgencia.servicies.interfaces.NotificacionAlertaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionAlertaServiceImpl extends ServiceImpl<NotificacionAlerta, Integer> implements NotificacionAlertaService {
    private final NotificacionAlertaRepository notificacionRepository;

    public NotificacionAlertaServiceImpl(NotificacionAlertaRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public void add(NotificacionAlerta notificacion){
        this.notificacionRepository.save(notificacion);
    }

    public void update(NotificacionAlerta notificacion){
        this.notificacionRepository.save(notificacion);
    }

    public NotificacionAlerta delete(Integer id){
        NotificacionAlerta notificacion = this.notificacionRepository.findById(id).orElseThrow();
        this.notificacionRepository.delete(notificacion);
        return notificacion;
    }

    public NotificacionAlerta findById(Integer id){
        return this.notificacionRepository.findById(id).orElseThrow();
    }

    public List<NotificacionAlerta> findAll(){
        return this.notificacionRepository.findAll();
    }

    public void crearNotificacion(NotificacionAlertaDTO n){
        NotificacionAlerta notificacion = new NotificacionAlerta(n.getMotivo(), n.getMensaje(), n.getVehiculoId());

        add(notificacion);
    }
}
