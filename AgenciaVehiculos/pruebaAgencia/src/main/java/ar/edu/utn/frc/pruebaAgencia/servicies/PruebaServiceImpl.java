package ar.edu.utn.frc.pruebaAgencia.servicies;

import ar.edu.utn.frc.pruebaAgencia.dto.NotificacionDTO;
import ar.edu.utn.frc.pruebaAgencia.dto.PosicionVehiculoDTO;
import ar.edu.utn.frc.pruebaAgencia.models.Empleado;
import ar.edu.utn.frc.pruebaAgencia.models.Interesado;
import ar.edu.utn.frc.pruebaAgencia.models.Prueba;
import ar.edu.utn.frc.pruebaAgencia.models.Vehiculo;
import ar.edu.utn.frc.pruebaAgencia.exceptions.PruebaException;
import ar.edu.utn.frc.pruebaAgencia.repositories.EmpleadoRepository;
import ar.edu.utn.frc.pruebaAgencia.repositories.InteresadoRepository;
import ar.edu.utn.frc.pruebaAgencia.repositories.PruebaRepository;
import ar.edu.utn.frc.pruebaAgencia.repositories.VehiculoRepository;
import ar.edu.utn.frc.pruebaAgencia.servicies.interfaces.PruebaService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PruebaServiceImpl extends ServiceImpl<Prueba, Integer> implements PruebaService {
    private final PruebaRepository pruebaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final InteresadoRepository interesadoRepository;
    private final EmpleadoRepository empleadoRepository;
    private RestTemplate restTemplate;

    public PruebaServiceImpl(PruebaRepository pruebaRepository, VehiculoRepository vehiculoRepository,
                             InteresadoRepository interesadoRepository, EmpleadoRepository empleadoRepository) {
        this.pruebaRepository = pruebaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.interesadoRepository = interesadoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public void add(Prueba prueba){
        this.pruebaRepository.save(prueba);
    }

    public void update(Prueba prueba){
        this.pruebaRepository.save(prueba);
    }

    public Prueba delete(Integer id){
        Prueba prueba = this.pruebaRepository.findById(id).orElseThrow();
        this.pruebaRepository.delete(prueba);
        return prueba;
    }

    public Prueba findById(Integer id){
        return this.pruebaRepository.findById(id).orElseThrow();
    }

    public List<Prueba> findAll(){
        return this.pruebaRepository.findAll();
    }

    public List<Prueba> listadoPruebasEnCurso() {
        return this.pruebaRepository.findAll()
                .stream()
                .filter(prueba -> prueba.getFechaFin().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public void crearPrueba(Prueba p){
        Interesado interesado = interesadoRepository.findById(p.getInteresado().getId())
                .orElseThrow(() -> new PruebaException("Cliente no encontrado!"));

        if (interesado.getFechaVencimientoLicencia().isBefore(LocalDateTime.now())){
            throw new PruebaException("La licencia se encuentra vencida!");
        }

        if (interesado.getRestringido()){
            throw new PruebaException("El cliente se encuentra restringido!");
        }

        Vehiculo vehiculo = vehiculoRepository.findById(p.getVehiculo().getId())
                .orElseThrow(() -> new PruebaException("Vehiculo no encontrado!"));

        boolean vehiculoEnPrueba = vehiculo.getPruebasVehiculo().stream()
                .anyMatch(v -> (v.getFechaFin().isAfter(LocalDateTime.now())));
        if (vehiculoEnPrueba){
            throw new PruebaException("El vehiculo tiene una prueba asignada!");
        }

        Empleado empleado = empleadoRepository.findById(p.getEmpleado().getLegajo())
                .orElseThrow(() -> new PruebaException("Empleado no encontrado!"));

        Prueba prueba = new Prueba(vehiculo, interesado, empleado, p.getFechaInicio(), p.getFechaFin());
        interesado.getPruebasInteresado().add(prueba);
        vehiculo.getPruebasVehiculo().add(prueba);
        empleado.getPruebasEmpleado().add(prueba);

        add(prueba);
    }

    public void agregarComentarios(int id, String comentarios){
        Prueba prueba = this.pruebaRepository.findById(id)
                .orElseThrow(() -> new PruebaException("Prueba no encontrada!"));

        prueba.setFechaFin(LocalDateTime.now());
        prueba.setComentarios(comentarios);

        update(prueba);
    }

    public void verificarEnviarNotificacion(){
        String url = "https://url-servicio-externo.com";
        PosicionVehiculoDTO posicionVehiculo = restTemplate.getForObject(url, PosicionVehiculoDTO.class);

        boolean estaEnLimite = evaluarPosicion(posicionVehiculo);
        if (!estaEnLimite){
            enviarNotificacion(posicionVehiculo);
        }
    }

    private void enviarNotificacion(PosicionVehiculoDTO posicion){
        NotificacionDTO notificacion = new NotificacionDTO(posicion.getVehiculoId(),
                "Exceso de limite", "Peligro! El vehiculo ha ingresado a una zona peligrosa o ha excedido el radio permitido");

        String url = "http://localhost:8084/api/notificaciones";
        restTemplate.postForObject(url, notificacion, String.class);
    }

    private boolean evaluarPosicion(PosicionVehiculoDTO posicion){
        boolean bool;
        Vehiculo v = vehiculoRepository.findById(posicion.getVehiculoId()).orElseThrow(() -> new PruebaException("Vehiculo no encontrado!"));
        Prueba prueba = v.getPruebasVehiculo().stream()
                .filter(p -> p.getFechaFin().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElse(null);

        if (prueba == null) {
            bool = false;
            throw new PruebaException("El vehiculo no tiene ninguna prueba activa");
        } else {
            double distancia = calcularDistancia();
            if (distancia > 0){
                bool = true;
            } else {
                bool = false;
                throw new PruebaException("El vehiculo esta dentro de los limites");
            }
        }

        return bool;
    }

    private double calcularDistancia() {
        //TODO
        return 0;
    }
}
