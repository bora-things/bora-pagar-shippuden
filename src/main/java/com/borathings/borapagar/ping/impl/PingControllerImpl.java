package com.borathings.borapagar.ping.impl;

import com.borathings.borapagar.ping.PingController;
import java.util.Collections;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Implementação do @PingController, Controller utilizado para fins de demonstração dos padrões a serem seguidos */
@RestController
public class PingControllerImpl implements PingController {
    @Override
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Pong");
    }

    @Override
    public ResponseEntity<List<String>> pings(@RequestParam int quantity) {
        List<String> response = Collections.nCopies(quantity, "pong");
        return ResponseEntity.ok().body(response);
    }
}
