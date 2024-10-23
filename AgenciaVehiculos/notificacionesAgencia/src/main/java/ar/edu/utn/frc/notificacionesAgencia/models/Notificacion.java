package ar.edu.utn.frc.notificacionesAgencia.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Notificaciones")
@Data
@NoArgsConstructor
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "motivo")
    private String motivoNotifacion;

    @Column(name = "mensaje")
    private String mensajeEnviado;

    @Column(name = "prueba_id")
    private int pruebaId;

    public Notificacion(String motivoNotifacion, String mensajeEnviado, int pruebaId) {
        this.motivoNotifacion = motivoNotifacion;
        this.mensajeEnviado = mensajeEnviado;
        this.pruebaId = pruebaId;
    }
}
