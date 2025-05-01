package ar.edu.utn.frc.pruebaAgencia.servicies;

import ar.edu.utn.frc.pruebaAgencia.client.ApiClient;
import ar.edu.utn.frc.pruebaAgencia.dto.AgenciaDTO;
import ar.edu.utn.frc.pruebaAgencia.dto.CoordenadaDTO;
import ar.edu.utn.frc.pruebaAgencia.dto.NotificacionAlertaDTO;
import ar.edu.utn.frc.pruebaAgencia.dto.ZonaRestringidaDTO;
import ar.edu.utn.frc.pruebaAgencia.models.*;
import ar.edu.utn.frc.pruebaAgencia.exceptions.PruebaException;
import ar.edu.utn.frc.pruebaAgencia.repositories.*;
import ar.edu.utn.frc.pruebaAgencia.servicies.interfaces.PruebaService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PruebaServiceImpl extends ServiceImpl<Prueba, Integer> implements PruebaService {
    private final PruebaRepository pruebaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final InteresadoRepository interesadoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final IncidenteServiceImpl incidenteService;
    private final RestTemplate restTemplate;
    private final ApiClient apiClient;

    public PruebaServiceImpl(PruebaRepository pruebaRepository, VehiculoRepository vehiculoRepository,
                             InteresadoRepository interesadoRepository, EmpleadoRepository empleadoRepository,
                             IncidenteServiceImpl incidenteService, RestTemplate restTemplate, ApiClient apiClient) {
        this.pruebaRepository = pruebaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.interesadoRepository = interesadoRepository;
        this.empleadoRepository = empleadoRepository;
        this.incidenteService = incidenteService;
        this.restTemplate = restTemplate;
        this.apiClient = apiClient;
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
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new PruebaException("No se encontro el vehiculo"));
        List<Posicion> posiciones = vehiculo.getPosicionesVehiculo();
        List<Prueba> pruebas = vehiculo.getPruebasVehiculo();

        Posicion posicionVehiculo = posiciones.stream()
                .max(Comparator.comparing(Posicion::getFechaHora))
                .orElseThrow(() -> new NoSuchElementException("No tiene posiciones guardadas"));

        Prueba prueba = pruebas.stream()
                .filter(p -> p.getFechaFin().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new PruebaException("El vehiculo no tiene una prueba activa"));

        boolean estaEnLimite = evaluarPosicionLimite(posicionVehiculo);
        boolean estaEnZona = evaluarPosicionRestringida(posicionVehiculo);
        if (estaEnLimite){
            Incidente incidente = new Incidente(prueba, prueba.getEmpleado());
            incidenteService.add(incidente);
            return enviarNotificacion(posicionVehiculo, "Exceso de limite", "Peligro! El vehiculo ha excedido el radio permitido");
        } else if (estaEnZona){
            return enviarNotificacion(posicionVehiculo, "Zona peligrosa", "Peligro! El vehiculo ha ingresado a una zona peligrosa");
        }
        return null;
    }

    private NotificacionAlertaDTO enviarNotificacion(Posicion posicion, String motivo, String mensaje){
        NotificacionAlertaDTO notificacion = new NotificacionAlertaDTO(
                motivo,
                mensaje,
                posicion.getVehiculo().getId());

        String url = "http://localhost:8084/api/notificaciones/enviar-notificacion";
        try {
            restTemplate.postForObject(url, notificacion, String.class);
            return notificacion;
        } catch (Exception e) {
            System.err.println("Error al enviar la notificación: " + e.getMessage());
            return null;
        }
    }

    private boolean evaluarPosicionLimite(Posicion posicion){
        double latAgencia = apiClient.getAgenciaInfo().getCoordenadasAgencia().getLat();
        double lonAgencia = apiClient.getAgenciaInfo().getCoordenadasAgencia().getLon();

        double distancia = calcularDistancia(latAgencia, lonAgencia, posicion.getLatitud(), posicion.getLongitud());

        return distancia >= 5.0;
    }

    private boolean evaluarPosicionRestringida(Posicion posicion){
        AgenciaDTO agencia = apiClient.getAgenciaInfo();
        List<ZonaRestringidaDTO> listaZonas = agencia.getZonasRestringidas();
        boolean zonaPeligrosa = false;

        for (int i = 0; i < listaZonas.size(); i++) {
            CoordenadaDTO noroeste = listaZonas.get(i).getNoroeste();
            CoordenadaDTO sureste = listaZonas.get(i).getSureste();

            if (sureste.getLat() <= posicion.getLatitud() && posicion.getLatitud() <= noroeste.getLat() &&
                    noroeste.getLon() <= posicion.getLongitud() && posicion.getLongitud() <= sureste.getLon()){
                zonaPeligrosa = true;
                break;
            }
        }
        return zonaPeligrosa;
    }

    private double calcularDistancia(double latOrigen, double lonOrigen, double latDestino, double lonDestino) {
        final double EARTH_RADIUS_KM = 6371.0;

        double lat1Rad = Math.toRadians(latOrigen);
        double lon1Rad = Math.toRadians(lonOrigen);
        double lat2Rad = Math.toRadians(latDestino);
        double lon2Rad = Math.toRadians(lonDestino);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public double calcularKmRecorrido(int idVehiculo, Date fecha){
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new PruebaException("No se encontro el vehiculo"));

        LocalDateTime fechaInicio = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<Posicion> posiciones = vehiculo.getPosicionesVehiculo().stream()
                .filter(p -> p.getFechaHora().isAfter(fechaInicio) || p.getFechaHora().isEqual(fechaInicio))
            .toList();

        if (posiciones.isEmpty()){
            return 0.0;
        }

        double distanciaTotal = 0.0;
        double latitudAgencia = apiClient.getAgenciaInfo().getCoordenadasAgencia().getLat();
        double longitudAgencia = apiClient.getAgenciaInfo().getCoordenadasAgencia().getLon();

        distanciaTotal += calcularDistancia(latitudAgencia, longitudAgencia, posiciones.get(0).getLatitud(), posiciones.get(0).getLongitud());

        for (int i = 1; i < posiciones.size(); i++) {
            distanciaTotal += calcularDistancia(posiciones.get(i-1).getLatitud(), posiciones.get(i-1).getLongitud(),
                    posiciones.get(i).getLatitud(), posiciones.get(i).getLongitud());
        }

        distanciaTotal += calcularDistancia(posiciones.getLast().getLatitud(), posiciones.getLast().getLongitud(),
                latitudAgencia, longitudAgencia);

        return distanciaTotal;
    }
}
