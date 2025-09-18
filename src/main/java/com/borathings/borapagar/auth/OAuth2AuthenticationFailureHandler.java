package com.borathings.borapagar.auth;

import com.borathings.borapagar.auth.exceptions.WhiteListException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Value("${frontend.url}")
    String frontendUrl;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String redirectUrl;

        if (exception instanceof WhiteListException) {
            redirectUrl = frontendUrl + "/acesso-negado";
        } else {
            redirectUrl = frontendUrl + "/login-error";
        }

        request.getSession().invalidate();

        response.sendRedirect(redirectUrl);
    }
}
