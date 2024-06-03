package com.puente.web.controller;

import com.puente.client.ClientService04;
import com.puente.model.RespService04;
import com.puente.web.config.ConfigService04;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Service04Controller {
    private ClientService04 clientService04;

    @Autowired
    public Service04Controller(ClientService04 clientService04) {
        this.clientService04 = clientService04;
    }

    @GetMapping("/get")
    public ResponseEntity<RespService04> get(){
        return ResponseEntity.ok(this.clientService04.response());
    }
}
