package com.borathings.borapagar.auth;

import com.borathings.borapagar.user.UserService;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** CustomOidcUserService */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CustomOAuth2UserService(@Value("${sigaa.api-key}") String apiKey) {
        RestTemplate client = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = List.of(new AddApiKeyInterceptor(apiKey));
        client.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        client.setInterceptors(interceptors);
        this.setRestOperations(client);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        logger.info(
                "Usuário com ID SIGAA {} e email {} carregado com sucesso",
                oAuth2User.getAttribute("nome-pessoa"),
                oAuth2User.getAttribute("email"));
        userService.upsert(oAuth2User);
        return oAuth2User;
    }
}

class AddApiKeyInterceptor implements ClientHttpRequestInterceptor {
    private String apiKey;

    public AddApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().set("X-API-KEY", apiKey);
        return execution.execute(request, body);
    }
}
