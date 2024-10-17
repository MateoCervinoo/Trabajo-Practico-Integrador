package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Prueba;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.PruebaRepository;

import java.util.List;

public class PruebaService implements Service<Prueba, Integer> {
    private final PruebaRepository pruebaRepository;

    public PruebaService(PruebaRepository pruebaRepository) {
        this.pruebaRepository = pruebaRepository;
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
}
