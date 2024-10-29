package ar.edu.utn.frc.notificacionesAgencia.servicies;

import ar.edu.utn.frc.notificacionesAgencia.models.NotificacionOferta;
import ar.edu.utn.frc.notificacionesAgencia.repositories.NotificacionOfertaRepository;
import ar.edu.utn.frc.notificacionesAgencia.servicies.interfaces.NotificacionOfertaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionOfertaServiceImpl  extends ServiceImpl<NotificacionOferta, Integer> implements NotificacionOfertaService {
    private final NotificacionOfertaRepository notificacionRepository;

    public NotificacionOfertaServiceImpl(NotificacionOfertaRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public void add(NotificacionOferta notificacion){
        this.notificacionRepository.save(notificacion);
    }

    public void update(NotificacionOferta notificacion){
        this.notificacionRepository.save(notificacion);
    }

    public NotificacionOferta delete(Integer id){
        NotificacionOferta notificacion = this.notificacionRepository.findById(id).orElseThrow();
        this.notificacionRepository.delete(notificacion);
        return notificacion;
    }

    public NotificacionOferta findById(Integer id){
        return this.notificacionRepository.findById(id).orElseThrow();
    }

    public List<NotificacionOferta> findAll(){
        return this.notificacionRepository.findAll();
    }

    public void crearNotificacion(NotificacionOferta n){
        NotificacionOferta notificacion = new NotificacionOferta(n.getMensajeEnviado());

        add(notificacion);
    }
}
