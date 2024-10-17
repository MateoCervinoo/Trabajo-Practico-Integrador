package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Vehiculo;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.VehiculoRepository;

import java.util.List;

public class VehiculoService implements Service<Vehiculo, Integer> {
    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public void add(Vehiculo vehiculo){
        this.vehiculoRepository.save(vehiculo);
    }

    public void update(Vehiculo vehiculo){
        this.vehiculoRepository.save(vehiculo);
    }

    public Vehiculo delete(Integer id){
        Vehiculo vehiculo = this.vehiculoRepository.findById(id).orElseThrow();
        this.vehiculoRepository.delete(vehiculo);
        return vehiculo;
    }

    public Vehiculo findById(Integer id){
        return this.vehiculoRepository.findById(id).orElseThrow();
    }

    public List<Vehiculo> findAll(){
        return this.vehiculoRepository.findAll();
    }
}
