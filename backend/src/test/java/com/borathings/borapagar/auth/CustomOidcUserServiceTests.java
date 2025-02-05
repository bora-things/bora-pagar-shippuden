package com.borathings.borapagar.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.borathings.borapagar.user.UserRepository;
import com.borathings.borapagar.user.UserService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

public class CustomOidcUserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private OidcUserService oidcUserService;

    @Mock
    private ClientRegistration clientRegistration;

    @Mock
    private ProviderDetails providerDetails;

    @Mock
    private OAuth2AccessToken accessToken;

    @Mock
    private UserInfoEndpoint userInfoEndpoint;

    @InjectMocks
    private CustomOidcUserService customOidcUserService;

    private OidcUserRequest oidcUserRequest;

    private OidcUser oidcUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        OidcIdToken idToken = new OidcIdToken("idToken", null, null, Collections.singletonMap("sub", "123456"));

        oidcUser = new DefaultOidcUser(Collections.singletonList(new OidcUserAuthority(idToken)), idToken);

        when(clientRegistration.getRegistrationId()).thenReturn("google");
        when(clientRegistration.getProviderDetails()).thenReturn(providerDetails);
        when(providerDetails.getUserInfoEndpoint()).thenReturn(userInfoEndpoint);
        when(userInfoEndpoint.getUri()).thenReturn("https://www.googleapis.com/oauth2/v3/userinfo");

        oidcUserRequest = new OidcUserRequest(clientRegistration, accessToken, idToken, null);
    }

    @Test
    public void testCallUpsertFromOidcUser() throws OAuth2AuthenticationException {
        when(oidcUserService.loadUser(any(OidcUserRequest.class))).thenReturn(oidcUser);

        customOidcUserService.loadUser(oidcUserRequest);

        verify(userService).upsertFromOidcUser(oidcUser);
    }
}
