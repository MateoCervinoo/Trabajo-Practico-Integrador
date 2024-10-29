package ar.edu.utn.frc.notificacionesAgencia.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificaciones_ofertas")
@Data
@NoArgsConstructor
public class NotificacionOferta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mensaje")
    private String mensajeEnviado;

    public NotificacionOferta(String mensajeEnviado) {
        this.mensajeEnviado = mensajeEnviado;
    }
}
