package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Empleado;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.EmpleadoRepository;

import java.util.List;

public class EmpleadoService implements Service<Empleado, Integer> {
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void add(Empleado empleado){
        this.empleadoRepository.save(empleado);
    }

    public void update(Empleado empleado){
        this.empleadoRepository.save(empleado);
    }

    public Empleado delete(Integer id){
        Empleado empleado = this.empleadoRepository.findById(id).orElseThrow();
        this.empleadoRepository.delete(empleado);
        return empleado;
    }

    public Empleado findById(Integer id){
        return this.empleadoRepository.findById(id).orElseThrow();
    }

    public List<Empleado> findAll(){
        return this.empleadoRepository.findAll();
    }
}
