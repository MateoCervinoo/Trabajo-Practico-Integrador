package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Empleado;
import ar.edu.utn.frc.AgenciaVehiculos.entities.Interesado;
import ar.edu.utn.frc.AgenciaVehiculos.entities.Prueba;
import ar.edu.utn.frc.AgenciaVehiculos.entities.Vehiculo;
import ar.edu.utn.frc.AgenciaVehiculos.exceptions.PruebaException;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.EmpleadoRepository;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.InteresadoRepository;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.PruebaRepository;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.VehiculoRepository;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.interfaces.PruebaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PruebaServiceImpl extends ServiceImpl<Prueba, Integer> implements PruebaService {
    private final PruebaRepository pruebaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final InteresadoRepository interesadoRepository;
    private final EmpleadoRepository empleadoRepository;

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
}
