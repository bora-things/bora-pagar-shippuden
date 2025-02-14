package com.borathings.borapagar.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * OAuth2AuthenticationSuccessHandler * Classe chamada quando a autentição OAuth2 foi sucedida
 *
 * <p>É utilizada para redicionar o usuário de volta para onde a requisição original foi feita
 */
/** OAuth2AuthenticationSuccessHandler */
@Component
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${frontend.redirect}")
    String frontendRedirect;

    /** Redireciona de volta o usuário para onde a requisição original foi feita */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            String originalReferer = (String) request.getSession().getAttribute("original_referer");
            request.getSession().removeAttribute("original_referer");
            response.sendRedirect(frontendRedirect);
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
