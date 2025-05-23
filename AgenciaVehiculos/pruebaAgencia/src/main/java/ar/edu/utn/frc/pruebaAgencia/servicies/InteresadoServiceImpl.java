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
import java.util.Random;

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
        Random random = new Random();
        Interesado interesado = findById(idInteresado);

        List<Prueba> pruebas = interesado.getPruebasInteresado();

        Prueba prueba = pruebas.stream()
                .filter(p -> p.getFechaFin().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElse(null);

        if (prueba == null){
            throw new PruebaException("El interesado no tiene una prueba activa");
        } else {
            double latitud = 42 + random.nextDouble();
            double longitud = 1 + random.nextDouble();

            Posicion posicion = new Posicion(prueba.getVehiculo(), LocalDateTime.now(), latitud, longitud);
            posicionService.add(posicion);
        }
    }
}
