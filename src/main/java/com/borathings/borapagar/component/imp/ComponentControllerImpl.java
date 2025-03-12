package com.borathings.borapagar.component.imp;

import com.borathings.borapagar.component.ComponentController;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.core.exception.ApiException;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComponentControllerImpl implements ComponentController {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private UserService userService;

    @Override
    public Void fetchComponents(Authentication auth) {
        UserEntity user = userService.findByLoginOrError(auth.getName());

        if (!user.isAdmin()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, new RuntimeException("Usuário não autorizado!"));
        }

        componentService.fetchComponents();

        return null;
    }
}
