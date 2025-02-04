package com.borathings.borapagar.config;

import com.borathings.borapagar.auth.CustomOAuth2AuthorizationRequestResolver;
import com.borathings.borapagar.auth.CustomOidcUserService;
import com.borathings.borapagar.auth.OAuth2AuthenticationSuccessHandler;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/** AuthConfig Class responsável por fazer configurações relacionadas ao spring-security */
@Configuration
@EnableWebSecurity
public class AuthConfig {
    @Autowired CustomOidcUserService customOidcUserService;
    @Autowired CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;
    @Autowired OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Value("${frontend.url}")
    String frontendUrl;

    @Bean
    /**
     * Pipeline usada pelo Spring Security, exige autenticação em todos os endpoints que tenham
     * <code>/api</code> como prefixo (Exceto a URL das docs do swagger, podemos alterar ela depois)
     * <code>oauth2Login()</code> Adiciona endpoints de autenticação com o google <code>
     * oauth2ResourceServer()</code> Configura que nossa API será um resource server na arquitetura
     * OAuth2, exigindo um token para que os dados sejam retornados Com a união desses dois métodos,
     * ao logar na url do backend, o cliente (frontend, app, bot) terá um cookie que será utilizado
     * como sessão, com isso enviar o token em toda request não é necessário
     *
     * @param HttpSecurity Classe usada para configurar o Spring Security
     * @throws Exception
     * @return SecurityFilterChain - Pipeline de segurança utilizada pelo Spring Security
     * @see <a
     *     href="https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html">OAuth2
     *     Login</a>
     * @see <a
     *     href="https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html"/>OAuth2
     *     Resource Server</a>
     */
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/api/v3/api-docs/**")
                                        .permitAll()
                                        .requestMatchers("/api/**")
                                        .authenticated()
                                        .anyRequest()
                                        .permitAll())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2Login(
                        oauthLogin ->
                                oauthLogin
                                        .userInfoEndpoint(
                                                userInfo ->
                                                        userInfo.oidcUserService(
                                                                customOidcUserService))
                                        .authorizationEndpoint(
                                                authorization ->
                                                        authorization.authorizationRequestResolver(
                                                                customOAuth2AuthorizationRequestResolver))
                                        .successHandler(oAuth2AuthenticationSuccessHandler))
                .exceptionHandling(
                        ex ->
                                ex.defaultAuthenticationEntryPointFor(
                                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                        new AntPathRequestMatcher("/api/**")));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
                new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", config);
        return urlBasedCorsConfigurationSource;
    }
}
