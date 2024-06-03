package com.puente.web.controller;



import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.service.SeguridadCanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seguridad")
public class SeguridadCanalController {
    private SeguridadCanalService seguridadService;

    @Autowired
    public SeguridadCanalController(SeguridadCanalService seguridadService) {
        this.seguridadService = seguridadService;
    }


    @GetMapping
    public ResponseEntity<List<SeguridadCanalEntity>> getAll(){
        return ResponseEntity.ok(this.seguridadService.getAll());
    }

    @GetMapping("/usuario")
    public ResponseEntity<SeguridadCanalEntity> get(@PathVariable String usuario){
        return ResponseEntity.ok(this.seguridadService.get(usuario));
    }
}
