package com.borathings.borapagar.auth;

import com.borathings.borapagar.student.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationEventListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StudentService studentService;

    @EventListener
    public void onAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            Long institutionalId = oauth2User.getAttribute("id-institucional");
            int userId = oauth2User.getAttribute("id-usuario");
            studentService.createFromInstitutionalId(institutionalId, userId);
        }
    }
}
