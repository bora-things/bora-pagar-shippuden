package com.borathings.borapagar.component.controller;

import com.borathings.borapagar.component.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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


}
