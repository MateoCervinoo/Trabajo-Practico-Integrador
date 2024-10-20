package ar.edu.utn.frc.AgenciaVehiculos.controllers;

import ar.edu.utn.frc.AgenciaVehiculos.entities.Empleado;
import ar.edu.utn.frc.AgenciaVehiculos.entities.Interesado;
import ar.edu.utn.frc.AgenciaVehiculos.entities.Prueba;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.EmpleadoServiceImpl;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.InteresadoServiceImpl;
import ar.edu.utn.frc.AgenciaVehiculos.servicies.PruebaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agencia")
public class PruebaController {
    private final EmpleadoServiceImpl empleadoService;
    private final InteresadoServiceImpl interesadoService;
    private final PruebaServiceImpl pruebaService;

    public PruebaController(EmpleadoServiceImpl empleadoService, InteresadoServiceImpl interesadoService, PruebaServiceImpl pruebaService) {
        this.empleadoService = empleadoService;
        this.interesadoService = interesadoService;
        this.pruebaService = pruebaService;
    }

    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> getAll() {
        List<Empleado> result = this.empleadoService.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/crear-cliente")
    public ResponseEntity<Object> addClient(@RequestBody Interesado interesado){
        try{
            this.interesadoService.add(interesado);
            return new ResponseEntity<>(interesado, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/crear-prueba")
    public ResponseEntity<Object> addPrueba(@RequestBody Prueba prueba){
        try{
            this.pruebaService.crearPrueba(prueba);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
