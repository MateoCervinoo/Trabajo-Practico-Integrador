package ar.edu.utn.frc.pruebaAgencia.servicies;

import ar.edu.utn.frc.pruebaAgencia.exceptions.PruebaException;
import ar.edu.utn.frc.pruebaAgencia.models.Interesado;
import ar.edu.utn.frc.pruebaAgencia.models.Posicion;
import ar.edu.utn.frc.pruebaAgencia.models.Prueba;
import ar.edu.utn.frc.pruebaAgencia.repositories.InteresadoRepository;
import ar.edu.utn.frc.pruebaAgencia.repositories.PosicionRepository;
import ar.edu.utn.frc.pruebaAgencia.servicies.interfaces.InteresadoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InteresadoServiceImpl extends ServiceImpl<Interesado, Integer> implements InteresadoService {
    private final InteresadoRepository interesadoRepository;
    private PosicionServiceImpl posicionService;

    public InteresadoServiceImpl(InteresadoRepository interesadoRepository, PosicionServiceImpl posicionService) {
        this.interesadoRepository = interesadoRepository;
        this.posicionService = posicionService;
    }

    public void add(Interesado interesado){
        this.interesadoRepository.save(interesado);
    }

    public void update(Interesado interesado){
        this.interesadoRepository.save(interesado);
    }

    public Interesado delete(Integer id){
        Interesado interesado = this.interesadoRepository.findById(id).orElseThrow();
        this.interesadoRepository.delete(interesado);
        return interesado;
    }

    public Interesado findById(Integer id){
        return this.interesadoRepository.findById(id).orElseThrow();
    }

    public List<Interesado> findAll(){
        return this.interesadoRepository.findAll();
    }

    public void guardarPosicionVehiculo(int idInteresado){
        Interesado interesado = findById(idInteresado);

        List<Prueba> pruebas = interesado.getPruebasInteresado();

        Prueba prueba = pruebas.stream()
                .filter(p -> p.getFechaFin().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElse(null);

        if (prueba == null){
            throw new PruebaException("El interesado no tiene una prueba activa");
        } else {
            double latitud = obtenerLatitudDesdeServicioExterno(prueba.getVehiculo().getId());
            double longitud = obtenerLongitudDesdeServicioExterno(prueba.getVehiculo().getId());

            Posicion posicion = new Posicion(prueba.getVehiculo(), LocalDateTime.now(), latitud, longitud);
            posicionService.add(posicion);
        }
    }

    // Métodos de ejemplo para obtener latitud y longitud desde el servicio externo
    private double obtenerLatitudDesdeServicioExterno(int vehiculoId) {
        return -34.603722;
    }

    private double obtenerLongitudDesdeServicioExterno(int vehiculoId) {
        return -58.381592;
    }
}
