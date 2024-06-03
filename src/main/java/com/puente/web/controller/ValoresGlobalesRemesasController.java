package com.puente.web.controller;


import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.ValoresGlobalesRemesasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/valores")
public class ValoresGlobalesRemesasController {
    private ValoresGlobalesRemesasService remesasService;

    @Autowired
    public ValoresGlobalesRemesasController(ValoresGlobalesRemesasService remesasService) {
        this.remesasService = remesasService;
    }

    @GetMapping
    public ResponseEntity<List<ValoresGlobalesRemesasEntity>> getAll(){
        return ResponseEntity.ok(this.remesasService.getAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ValoresGlobalesRemesasEntity> get(@PathVariable String codigo){
        return ResponseEntity.ok(this.remesasService.get(codigo));
    }

    @PostMapping
    public ResponseEntity<ValoresGlobalesRemesasEntity> save(@RequestBody ValoresGlobalesRemesasEntity entity){
        return ResponseEntity.ok(this.remesasService.save(entity));
    }
}
