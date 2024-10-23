package ar.edu.utn.frc.notificacionesAgencia.servicies;

import ar.edu.utn.frc.notificacionesAgencia.models.Notificacion;
import ar.edu.utn.frc.notificacionesAgencia.repositories.NotificacionRepository;
import ar.edu.utn.frc.notificacionesAgencia.servicies.interfaces.NotificacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionServiceImpl extends ServiceImpl<Notificacion, Integer> implements NotificacionService {
    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public void add(Notificacion notificacion){
        this.notificacionRepository.save(notificacion);
    }

    public void update(Notificacion notificacion){
        this.notificacionRepository.save(notificacion);
    }

    public Notificacion delete(Integer id){
        Notificacion notificacion = this.notificacionRepository.findById(id).orElseThrow();
        this.notificacionRepository.delete(notificacion);
        return notificacion;
    }

    public Notificacion findById(Integer id){
        return this.notificacionRepository.findById(id).orElseThrow();
    }

    public List<Notificacion> findAll(){
        return this.notificacionRepository.findAll();
    }

    public void crearNotificacion(Notificacion n){
        Notificacion notificacion = new Notificacion(n.getMotivoNotifacion(), n.getMensajeEnviado(), n.getPruebaId());

        add(notificacion);
    }
}
