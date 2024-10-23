package ar.edu.utn.frc.notificacionesAgencia.controllers;

import ar.edu.utn.frc.notificacionesAgencia.models.Notificacion;
import ar.edu.utn.frc.notificacionesAgencia.servicies.NotificacionServiceImpl;
import jakarta.persistence.Entity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    private final NotificacionServiceImpl notificacionService;

    public NotificacionController(NotificacionServiceImpl notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    public ResponseEntity<String> recibirNotificacion(@RequestBody Notificacion notificacion){
        try {
            notificacionService.crearNotificacion(notificacion);
            return new ResponseEntity<>("Notificacion guardada", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error al procesar la notificación.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
