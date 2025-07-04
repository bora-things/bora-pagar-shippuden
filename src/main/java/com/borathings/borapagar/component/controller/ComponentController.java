package com.borathings.borapagar.component.controller;

import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.dto.ComponentDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.borathings.borapagar.component.dto.ComponentDetailsDTO;
import com.borathings.borapagar.component.dto.ComponentResponseDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/components")
public class ComponentController {

    @Autowired
    ComponentService componentService;

    @GetMapping("/fetch")
    public void fetchComponents() {
        componentService.fetchComponents();
    }

    @GetMapping
    public ResponseEntity<List<ComponentDTO>> findSearchedComponents(@RequestParam String searched) {
        List<ComponentDTO> data = componentService.findSearchedComponents(searched);

        Set<String> processedComponentCodes = new HashSet<>();

        List<ComponentDTO> uniqueData = data.stream()
                .filter(component -> processedComponentCodes.add(component.code()))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(uniqueData);
    }

    @GetMapping("/details")
    public ResponseEntity<ComponentResponseDetailsDTO> findComponentDetails(@RequestParam String code, Authentication authentication) {
        String login = authentication.getName();
        ComponentResponseDetailsDTO detailsDTO= componentService.findComponentDetails(code,login);
        return ResponseEntity.ok(detailsDTO);
    }


}
