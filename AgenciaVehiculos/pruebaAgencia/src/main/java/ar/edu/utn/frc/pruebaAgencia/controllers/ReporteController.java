package ar.edu.utn.frc.pruebaAgencia.controllers;

import ar.edu.utn.frc.pruebaAgencia.dto.*;
import ar.edu.utn.frc.pruebaAgencia.models.Prueba;
import ar.edu.utn.frc.pruebaAgencia.models.Vehiculo;
import ar.edu.utn.frc.pruebaAgencia.servicies.IncidenteServiceImpl;
import ar.edu.utn.frc.pruebaAgencia.servicies.PruebaServiceImpl;
import ar.edu.utn.frc.pruebaAgencia.servicies.VehiculoServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/agencia/reportes")
public class ReporteController {
    private final VehiculoServiceImpl vehiculoService;
    private final PruebaServiceImpl pruebaService;
    private final IncidenteServiceImpl incidenteService;

    public ReporteController(VehiculoServiceImpl vehiculoService, IncidenteServiceImpl incidenteService,
                             PruebaServiceImpl pruebaService) {
        this.vehiculoService = vehiculoService;
        this.incidenteService = incidenteService;
        this.pruebaService = pruebaService;
    }

    @GetMapping("/pruebas-vehiculo/{id}")
    public ResponseEntity<Object> pruebasPorVehiculo(@PathVariable int id) {
        try {
            Vehiculo vehiculo = vehiculoService.findById(id);
            List<PruebaDTO> pruebaDTOs = vehiculo.getPruebasVehiculo().stream()
                    .map(p -> new PruebaDTO(p.getId(), p.getFechaFin(), new InteresadoDTO(p.getInteresado().getId(), p.getInteresado().getNombreInteresado(), p.getInteresado().getApellidoInteresado())))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(pruebaDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listado-incidentes")
    public ResponseEntity<List<PruebaDTO>> listadoIncidentes() {
        List<Prueba> pruebas = this.incidenteService.listadoPruebas();
        List<PruebaDTO> pruebaDTOs = pruebas.stream()
                .map(p -> new PruebaDTO(p.getId(), p.getFechaFin(), new InteresadoDTO(p.getInteresado().getId(), p.getInteresado().getNombreInteresado(), p.getInteresado().getApellidoInteresado()), new VehiculoDTO(p.getVehiculo().getPatente(), new ModeloDTO(p.getVehiculo().getModelo().getId(), new MarcaDTO(p.getVehiculo().getModelo().getMarca().getId(), p.getVehiculo().getModelo().getMarca().getNombre()), p.getVehiculo().getModelo().getDescripcion()), p.getVehiculo().getAnio())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pruebaDTOs);
    }

    @GetMapping("/listado-incidentes/{idEmpleado}")
    public ResponseEntity<List<PruebaDTO>> listadoIncidentes(@PathVariable int idEmpleado) {
        List<Prueba> pruebas = this.incidenteService.listadoPruebasEmpleado(idEmpleado);
        List<PruebaDTO> pruebaDTOs = pruebas.stream()
                .map(p -> new PruebaDTO(p.getId(), p.getFechaFin(), new InteresadoDTO(p.getInteresado().getId(), p.getInteresado().getNombreInteresado(), p.getInteresado().getApellidoInteresado()), new VehiculoDTO(p.getVehiculo().getPatente(), new ModeloDTO(p.getVehiculo().getModelo().getId(), new MarcaDTO(p.getVehiculo().getModelo().getMarca().getId(), p.getVehiculo().getModelo().getMarca().getNombre()), p.getVehiculo().getModelo().getDescripcion()), p.getVehiculo().getAnio())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pruebaDTOs);
    }

    @GetMapping("/cantidad-km/{idVehiculo}/{fechaInicio}")
    public ResponseEntity<Object> cantidadKilometros(@PathVariable int idVehiculo,
                                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio){
        double distanciaRecorrida = pruebaService.calcularKmRecorrido(idVehiculo, fechaInicio);
        if (distanciaRecorrida > 0.0){
            Vehiculo v = vehiculoService.findById(idVehiculo);
            VehiculoDTO vehiculoDTO = new VehiculoDTO(v.getPatente(), new ModeloDTO(v.getModelo().getId(), new MarcaDTO(v.getModelo().getMarca().getId(), v.getModelo().getMarca().getNombre()), v.getModelo().getDescripcion()), v.getAnio());
            KilometrajeRecorridoDTO kilometrajeRecorridoDTO = new KilometrajeRecorridoDTO(vehiculoDTO, distanciaRecorrida);

            return ResponseEntity.ok(kilometrajeRecorridoDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tiene posiciones guardadas");
        }
    }
}
