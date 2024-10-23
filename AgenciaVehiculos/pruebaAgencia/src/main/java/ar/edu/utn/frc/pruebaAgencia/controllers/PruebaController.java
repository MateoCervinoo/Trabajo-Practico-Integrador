package ar.edu.utn.frc.pruebaAgencia.controllers;

import ar.edu.utn.frc.pruebaAgencia.dto.*;
import ar.edu.utn.frc.pruebaAgencia.models.Prueba;
import ar.edu.utn.frc.pruebaAgencia.servicies.EmpleadoServiceImpl;
import ar.edu.utn.frc.pruebaAgencia.servicies.PruebaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agencia/prueba")
public class PruebaController {
    private final EmpleadoServiceImpl empleadoService;
    private final PruebaServiceImpl pruebaService;

    public PruebaController(EmpleadoServiceImpl empleadoService, PruebaServiceImpl pruebaService) {
        this.empleadoService = empleadoService;
        this.pruebaService = pruebaService;
    }

    @GetMapping("/listado")
    public ResponseEntity<List<PruebaDTO>> listadoPruebasEnCurso() {
        List<Prueba> pruebas = this.pruebaService.listadoPruebasEnCurso();
        List<PruebaDTO> pruebaDTOs = pruebas.stream()
                .map(p -> new PruebaDTO(p.getId(), p.getFechaFin(), new InteresadoDTO(p.getInteresado().getId(), p.getInteresado().getNombreInteresado(), p.getInteresado().getApellidoInteresado()), new VehiculoDTO(p.getVehiculo().getPatente(), new ModeloDTO(p.getVehiculo().getModelo().getId(), new MarcaDTO(p.getVehiculo().getModelo().getMarca().getId(), p.getVehiculo().getModelo().getMarca().getNombre()), p.getVehiculo().getModelo().getDescripcion()), p.getVehiculo().getAnio())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pruebaDTOs);
    }

    @PostMapping("/crear")
    public ResponseEntity<Object> agregarPrueba(@RequestBody Prueba prueba){
        try{
            this.pruebaService.crearPrueba(prueba);
            return new ResponseEntity<>("Prueba creada correctamente!", HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<Object> finalizarPrueba(@PathVariable int id, @RequestBody String comentarios) {
        try {
            pruebaService.agregarComentarios(id, comentarios);
            return new ResponseEntity<>("Prueba finalizada correctamente!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}