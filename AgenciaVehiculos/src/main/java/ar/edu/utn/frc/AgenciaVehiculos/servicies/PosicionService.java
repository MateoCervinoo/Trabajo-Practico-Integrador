package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Posicion;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.PosicionRepository;

import java.util.List;

public class PosicionService implements Service<Posicion, Integer> {
    private final PosicionRepository posicionRepository;

    public PosicionService(PosicionRepository posicionRepository) {
        this.posicionRepository = posicionRepository;
    }

    public void add(Posicion posicion){
        this.posicionRepository.save(posicion);
    }

    public void update(Posicion posicion){
        this.posicionRepository.save(posicion);
    }

    public Posicion delete(Integer id){
        Posicion posicion = this.posicionRepository.findById(id).orElseThrow();
        this.posicionRepository.delete(posicion);
        return posicion;
    }

    public Posicion findById(Integer id){
        return this.posicionRepository.findById(id).orElseThrow();
    }

    public List<Posicion> findAll(){
        return this.posicionRepository.findAll();
    }
}
