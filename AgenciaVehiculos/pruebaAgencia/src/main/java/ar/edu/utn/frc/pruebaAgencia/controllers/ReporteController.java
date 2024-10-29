package ar.edu.utn.frc.pruebaAgencia.controllers;

import ar.edu.utn.frc.pruebaAgencia.dto.*;
import ar.edu.utn.frc.pruebaAgencia.models.Vehiculo;
import ar.edu.utn.frc.pruebaAgencia.servicies.VehiculoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/agencia/reportes")
public class ReporteController {
    private final VehiculoServiceImpl vehiculoService;

    public ReporteController(VehiculoServiceImpl vehiculoService) {
        this.vehiculoService = vehiculoService;
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
}
