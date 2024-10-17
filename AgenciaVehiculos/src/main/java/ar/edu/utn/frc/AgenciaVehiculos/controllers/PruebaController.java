package ar.edu.utn.frc.AgenciaVehiculos.controllers;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Empleado;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.EmpleadoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agencia/prueba")
public class PruebaController {
    private final EmpleadoServiceImpl empleadoService;

    public PruebaController(EmpleadoServiceImpl empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> getAll() {
        List<Empleado> result = this.empleadoService.findAll();
        return ResponseEntity.ok(result);
    }
}
