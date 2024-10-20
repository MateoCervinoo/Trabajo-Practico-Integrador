package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Vehiculo;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.VehiculoRepository;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.interfaces.VehiculoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoServiceImpl extends ServiceImpl<Vehiculo, Integer> implements VehiculoService {
    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
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
