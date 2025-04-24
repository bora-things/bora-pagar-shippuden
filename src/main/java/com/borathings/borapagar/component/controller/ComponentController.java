package com.borathings.borapagar.component.controller;

import com.borathings.borapagar.task.ComponentFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/components")
public class ComponentController {

    @Autowired
    ComponentFetchService componentFetchService;

    @GetMapping("/fetch")
    public void fetchComponents() {
        componentFetchService.fetchComponents();
    }


}
