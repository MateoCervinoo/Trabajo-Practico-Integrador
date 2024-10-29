package ar.edu.utn.frc.notificacionesAgencia.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificaciones_alertas")
@Data
@NoArgsConstructor
public class NotificacionAlerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "motivo")
    private String motivoNotifacion;

    @Column(name = "mensaje")
    private String mensajeEnviado;

    @Column(name = "vehiculo_id")
    private int vehiculoId;

    public NotificacionAlerta(String motivoNotificacion, String mensajeEnviado, int vehiculoId) {
        this.motivoNotifacion = motivoNotificacion;
        this.mensajeEnviado = mensajeEnviado;
        this.vehiculoId = vehiculoId;
    }
}
