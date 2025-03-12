package com.borathings.borapagar.component;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/fetch-components")
public interface ComponentController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public Void fetchComponents(Authentication auth);

}
