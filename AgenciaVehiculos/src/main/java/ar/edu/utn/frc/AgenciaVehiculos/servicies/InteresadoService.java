package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Interesado;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.InteresadoRepository;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.interfaces.Service;

import java.util.List;

public class InteresadoService implements Service<Interesado, Integer> {
    private final InteresadoRepository interesadoRepository;

    public InteresadoService(InteresadoRepository interesadoRepository) {
        this.interesadoRepository = interesadoRepository;
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
}
