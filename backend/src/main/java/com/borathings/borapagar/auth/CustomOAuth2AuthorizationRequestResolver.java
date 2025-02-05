package com.borathings.borapagar.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

/**
 * CustomOAuth2AuthorizationRequestResolver
 *
 * <p>Classe criada para customizar o processo de resolução da authorization OAuth2
 */
/** CustomOAuth2AuthorizationRequestResolver */
@Component
public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    /**
     * Resolve a request com o resolver padrão, e chama o método <code>requestResolvedWithReferer
     * </code> para adicionar a URL de origem de onde o endpoint de autenticação foi chamado
     */
    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest originalRequest) {
        OAuth2AuthorizationRequest resolvedRequest = defaultResolver.resolve(originalRequest);
        return requestResolvedWithReferer(resolvedRequest, originalRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest originalRequest, String clientRegistrationId) {
        OAuth2AuthorizationRequest resolvedRequest = defaultResolver.resolve(originalRequest, clientRegistrationId);
        return requestResolvedWithReferer(resolvedRequest, originalRequest);
    }

    /**
     * @param resolvedRequest Requisição já resolvida pelo Spring Security
     * @param originalRequest Requisição antes do redirect para authorization server (Google)
     *     <p>Segundo o protocolo do OAuth2, o argumento <code>state</code> que vem como query param pode ser utilizado
     *     para passar informações extras. Porém o spring-security optou por não implementar esse comportamento,
     *     portanto ao invés de utilizar state, o Referer (Header de onde a requisição foi feita) É adicionado à sessão
     *     atual
     * @see <a href=https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.1>RFC OAuth2</a>
     * @see <a href=https://github.com/spring-projects/spring-security/issues/7808#issuecomment-704548457>Sugestão do
     *     maintainer do Spring-Security</a>
     */
    private OAuth2AuthorizationRequest requestResolvedWithReferer(
            OAuth2AuthorizationRequest resolvedRequest, HttpServletRequest originalRequest) {
        if (resolvedRequest == null) {
            return null;
        }
        String referer = originalRequest.getHeader("Referer");
        originalRequest.getSession().setAttribute("original_referer", referer);

        logger.info("Autenticação vinda do cliente {} foi resolvida", referer);

        return resolvedRequest;
    }
}
