package ar.edu.utn.frc.pruebaAgencia.servicies;

import ar.edu.utn.frc.pruebaAgencia.dto.NotificacionAlertaDTO;
import ar.edu.utn.frc.pruebaAgencia.models.*;
import ar.edu.utn.frc.pruebaAgencia.exceptions.PruebaException;
import ar.edu.utn.frc.pruebaAgencia.repositories.*;
import ar.edu.utn.frc.pruebaAgencia.servicies.interfaces.PruebaService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PruebaServiceImpl extends ServiceImpl<Prueba, Integer> implements PruebaService {
    private final PruebaRepository pruebaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final InteresadoRepository interesadoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PosicionRepository posicionRepository;
    private final RestTemplate restTemplate;

    public PruebaServiceImpl(PruebaRepository pruebaRepository, VehiculoRepository vehiculoRepository,
                             InteresadoRepository interesadoRepository, EmpleadoRepository empleadoRepository,
                             PosicionRepository posicionRepository, RestTemplate restTemplate) {
        this.pruebaRepository = pruebaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.interesadoRepository = interesadoRepository;
        this.empleadoRepository = empleadoRepository;
        this.posicionRepository = posicionRepository;
        this.restTemplate = restTemplate;
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

    public NotificacionAlertaDTO verificarEnviarNotificacion(int idVehiculo){
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElseThrow();
        List<Posicion> posiciones = vehiculo.getPosicionesVehiculo();

        Posicion posicionVehiculo = posiciones.stream()
                .max(Comparator.comparing(Posicion::getFechaHora))
                .orElseThrow(() -> new NoSuchElementException("No tiene posiciones guardadas"));

        boolean estaEnLimite = evaluarPosicion(posicionVehiculo);
        if (!estaEnLimite){
            return enviarNotificacion(posicionVehiculo);
        }
        return null;
    }

    private NotificacionAlertaDTO enviarNotificacion(Posicion posicion){
        NotificacionAlertaDTO notificacion = new NotificacionAlertaDTO(
                "Exceso de limite",
                "Peligro! El vehiculo ha ingresado a una zona peligrosa o ha excedido el radio permitido",
                posicion.getVehiculo().getId());

        String url = "http://localhost:8084/api/notificaciones";
        try {
            restTemplate.postForObject(url, notificacion, String.class);
            return notificacion;
        } catch (Exception e) {
            System.err.println("Error al enviar la notificación: " + e.getMessage());
            return null;
        }
    }

    private boolean evaluarPosicion(Posicion posicion){
        Vehiculo vehiculo = vehiculoRepository.findById(posicion.getVehiculo().getId())
                .orElseThrow(() -> new PruebaException("Vehículo no encontrado"));

        Prueba pruebaActiva = vehiculo.getPruebasVehiculo().stream()
                .filter(p -> p.getFechaFin().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new PruebaException("El vehículo no tiene ninguna prueba activa"));


        double distancia = calcularDistancia();
        //TODO;
        return false;
        // distancia > pruebaActiva.getRadioPermitido();
    }

    private double calcularDistancia() {
        //TODO
        return 0;
    }
}
