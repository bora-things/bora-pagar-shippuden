package com.borathings.borapagar.config;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    @Value("${sigaa.api-key}")
    private String apiKey;

    @Value("${sigaa.client-id}")
    private String clientId;

    @Value("${sigaa.client-secret}")
    private String clientSecret;

    @Value("${sigaa.token-uri}")
    private String tokenUri;

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .build());

        return authorizedClientManager;
    }

    @Bean(name = "userRestClient")
    public RestClient UserRestClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        return RestClient.builder()
                .baseUrl("https://api.info.ufrn.br")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-API-KEY", apiKey)
                .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(authorizedClientManager))
                .build();
    }

    @Bean(name = "serviceRestClient")
    public RestClient ServiceRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.info.ufrn.br")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-API-KEY", apiKey)
                .requestInterceptor((request, body, execution) -> {
                    String token = getServiceToken();
                    request.getHeaders().setBearerAuth(token);
                    return execution.execute(request, body);
                })
                .build();
    }

    private String getServiceToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);

        return (String) response.getBody().get("access_token");
    }
}
