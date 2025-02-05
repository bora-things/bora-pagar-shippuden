package com.borathings.borapagar.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.test.util.ReflectionTestUtils;

class CustomOAuth2AuthorizationRequestResolverTest {
    @Mock
    private ClientRegistrationRepository clientRegistrationRepository;

    @Mock
    private OAuth2AuthorizationRequestResolver defaultResolver;

    @InjectMocks
    private CustomOAuth2AuthorizationRequestResolver customResolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        /* defaultResolver é um atributo privado criado com base no clientRegistrationRepository,
         * Não é possível realizar o mock diretamente por isso utilizei do ReflectionTestUtils
         */
        ReflectionTestUtils.setField(customResolver, "defaultResolver", defaultResolver);
    }

    @Test
    void testResolveWithReferer() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Referer", "borapagar.com");
        request.setSession(new MockHttpSession());

        OAuth2AuthorizationRequest mockRequest = mock(OAuth2AuthorizationRequest.class);
        when(defaultResolver.resolve(request)).thenReturn(mockRequest);

        OAuth2AuthorizationRequest result = customResolver.resolve(request);

        verify(defaultResolver).resolve(request);
        assertEquals("borapagar.com", request.getSession().getAttribute("original_referer"));
        assertEquals(mockRequest, result);
    }

    @Test
    void testResolveWithoutReferer() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());

        OAuth2AuthorizationRequest mockRequest = mock(OAuth2AuthorizationRequest.class);
        when(defaultResolver.resolve(request)).thenReturn(mockRequest);

        OAuth2AuthorizationRequest result = customResolver.resolve(request);

        verify(defaultResolver).resolve(request);

        assertNull(request.getSession().getAttribute("original_referer"));
        assertEquals(mockRequest, result);
    }

    @Test
    void testResolverWithClientRegistrationId() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Referer", "borapagar.com");
        request.setSession(new MockHttpSession());

        OAuth2AuthorizationRequest mockRequest = mock(OAuth2AuthorizationRequest.class);
        when(defaultResolver.resolve(request, "google")).thenReturn(mockRequest);

        OAuth2AuthorizationRequest result = customResolver.resolve(request, "google");

        verify(defaultResolver).resolve(request, "google");
        assertEquals("borapagar.com", request.getSession().getAttribute("original_referer"));
        assertEquals(mockRequest, result);
    }

    @Test
    void testResolveNullRequest() {
        OAuth2AuthorizationRequest result = customResolver.resolve(null);

        assertEquals(null, result);
    }
}
