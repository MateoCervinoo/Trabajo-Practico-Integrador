package ar.edu.utn.frc.pruebaAgencia.controllers;

import ar.edu.utn.frc.pruebaAgencia.models.Interesado;
import ar.edu.utn.frc.pruebaAgencia.servicies.InteresadoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agencia/interesado")
public class InteresadoController {
    private final InteresadoServiceImpl interesadoService;

    public InteresadoController(InteresadoServiceImpl interesadoService) {
        this.interesadoService = interesadoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Object> addClient(@RequestBody Interesado interesado){
        try{
            this.interesadoService.add(interesado);
            return new ResponseEntity<>("Interesado creado correctamente!", HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
