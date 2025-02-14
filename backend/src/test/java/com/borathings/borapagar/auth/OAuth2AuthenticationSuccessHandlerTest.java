package com.borathings.borapagar.auth;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

class OAuth2AuthenticationSuccessHandlerTest {

    private OAuth2AuthenticationSuccessHandler successHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private OAuth2AuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        successHandler = new OAuth2AuthenticationSuccessHandler();
    }

    // Teste não é mais necessário pois será redirecionado para a variavel ${frontend.redirect}

    //    @Test
    //    void testOnAuthenticationSuccessWithOriginalReferer() throws IOException, ServletException {
    //        when(request.getSession()).thenReturn(session);
    //        when(request.getSession().getAttribute("original_referer")).thenReturn("borapagar.com");
    //
    //        successHandler.onAuthenticationSuccess(request, response, authentication);
    //
    //        verify(response).sendRedirect("borapagar.com");
    //    }

    @Test
    void testOnAuthenticationSuccessWithoutOriginalReferer() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("original_referer")).thenReturn(null);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void testDontAddSessionAttributeWhenNotOAuth2() throws IOException, ServletException {
        Authentication authentication = mock(Authentication.class);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("original_referer")).thenReturn("borapagar.com");
        successHandler.onAuthenticationSuccess(request, response, authentication);
        verify(response).sendRedirect(null);
    }
}
