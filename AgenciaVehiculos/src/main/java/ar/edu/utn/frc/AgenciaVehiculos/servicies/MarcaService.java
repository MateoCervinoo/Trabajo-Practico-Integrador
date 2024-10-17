package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Marca;
import ar.edu.utn.frc.AgenciaVehiculos.repositories.MarcaRepository;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.interfaces.Service;

import java.util.List;

public class MarcaService implements Service<Marca, Integer> {
    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public void add(Marca marca){
        this.marcaRepository.save(marca);
    }

    public void update(Marca marca){
        this.marcaRepository.save(marca);
    }

    public Marca delete(Integer id){
        Marca marca = this.marcaRepository.findById(id).orElseThrow();
        this.marcaRepository.delete(marca);
        return marca;
    }

    public Marca findById(Integer id){
        return this.marcaRepository.findById(id).orElseThrow();
    }

    public List<Marca> findAll(){
        return this.marcaRepository.findAll();
    }
}
