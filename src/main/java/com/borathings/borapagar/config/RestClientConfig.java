package com.borathings.borapagar.config;

// Imports do Jackson para a correção
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Qualifier; // Import necessário
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter; // Import para o conversor
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

    @Value("${sigaa.api-base-url}")
    private String apiBaseUrl;

    private final RestTemplate tokenRestTemplate = new RestTemplate();
    private final Lock serviceTokenLock = new ReentrantLock();
    private volatile String cachedServiceToken = null;
    private volatile Instant tokenExpiryTime = null;

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

    @Bean
    @Qualifier("sigaaApiObjectMapper")
    public ObjectMapper sigaaApiObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    @Bean(name = "userRestClient")
    public RestClient UserRestClient(
            OAuth2AuthorizedClientManager authorizedClientManager,
            @Qualifier("sigaaApiObjectMapper") ObjectMapper objectMapper) {

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        return RestClient.builder()
                .baseUrl(apiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-API-KEY", apiKey)
                .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(authorizedClientManager))
                .messageConverters(converters -> {
                    converters.clear();
                    converters.add(converter);
                })
                .build();
    }

    @Bean(name = "serviceRestClient")
    public RestClient ServiceRestClient(@Qualifier("sigaaApiObjectMapper") ObjectMapper objectMapper) {

        // Cria um conversor de mensagem que usa o ObjectMapper customizado
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        return RestClient.builder()
                .baseUrl(apiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-API-KEY", apiKey)
                .requestInterceptor((request, body, execution) -> {
                    String token = getServiceToken();
                    request.getHeaders().setBearerAuth(token);
                    return execution.execute(request, body);
                })
                .messageConverters(converters -> {
                    converters.clear();
                    converters.add(converter);
                })
                .build();
    }

    private String getServiceToken() {
        if (cachedServiceToken != null && Instant.now().isBefore(tokenExpiryTime)) {
            return cachedServiceToken;
        }

        serviceTokenLock.lock();
        try {
            if (cachedServiceToken != null && Instant.now().isBefore(tokenExpiryTime)) {
                return cachedServiceToken;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = tokenRestTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);
            Map<String, Object> responseBody = Objects.requireNonNull(response.getBody());

            this.cachedServiceToken = (String) responseBody.get("access_token");
            Integer expiresIn = (Integer) responseBody.get("expires_in");

            this.tokenExpiryTime = Instant.now().plusSeconds(expiresIn - 60);

            return this.cachedServiceToken;
        } finally {
            serviceTokenLock.unlock();
        }
    }
}
