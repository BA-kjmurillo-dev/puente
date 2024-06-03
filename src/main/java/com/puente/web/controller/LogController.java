package com.puente.web.controller;


import com.puente.persistence.entity.LogEntity;
import com.puente.persistence.entity.LogId;
import com.puente.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {
    private LogService logService;


    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<LogEntity>> getAll(){
        return ResponseEntity.ok(this.logService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<LogEntity> get(@RequestBody LogId logId){
        LogId log = new LogId(logId.getRemesa(), logId.getFechaLog(), logId.getHoraLog(), logId.getCanal());
        return ResponseEntity.ok(this.logService.get(log));
    }
}
