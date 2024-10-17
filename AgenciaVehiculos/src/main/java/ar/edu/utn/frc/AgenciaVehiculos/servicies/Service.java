package ar.edu.utn.frc.AgenciaVehiculos.servicies;

import java.util.List;

public interface Service<T, ID> {
    void add(T entity);
    void update(T entity);
    T delete(ID id);
    T findById(ID id);
    List<T> findAll();
}
