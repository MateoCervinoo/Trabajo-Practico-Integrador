package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Modelo;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.ModeloRepository;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.interfaces.Service;

import java.util.List;

public class ModeloService implements Service<Modelo, Integer> {
    private final ModeloRepository modeloRepository;

    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    public void add(Modelo modelo){
        this.modeloRepository.save(modelo);
    }

    public void update(Modelo modelo){
        this.modeloRepository.save(modelo);
    }

    public Modelo delete(Integer id){
        Modelo modelo = this.modeloRepository.findById(id).orElseThrow();
        this.modeloRepository.delete(modelo);
        return modelo;
    }

    public Modelo findById(Integer id){
        return this.modeloRepository.findById(id).orElseThrow();
    }

    public List<Modelo> findAll(){
        return this.modeloRepository.findAll();
    }
}
