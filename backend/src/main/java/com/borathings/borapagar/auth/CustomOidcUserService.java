package com.borathings.borapagar.auth;

import com.borathings.borapagar.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/** CustomOidcUserService */
@Service
public class CustomOidcUserService extends OidcUserService {
    @Autowired UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        logger.info(
                "Usuário com ID SIGAA {} e email {} carregado com sucesso",
                oidcUser.getSubject(),
                oidcUser.getEmail());
        userService.upsertFromOidcUser(oidcUser);
        return oidcUser;
    }
}
