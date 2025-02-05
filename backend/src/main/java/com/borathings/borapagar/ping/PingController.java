package com.borathings.borapagar.ping;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Este é um exemplo de Controller a ser seguido no projeto. Adotamos o padrão descrito em
 * https://www.baeldung.com/spring-interface-driven-controllers, no qual as annotations necessárias são colocadas na
 * interface.
 *
 * <p>Esse padrão permite que a implementação dos Controllers seja mais "limpa" e focada apenas nos detalhes técnicos,
 * promovendo uma separação clara entre a definição da API e sua implementação.
 */
@RestControllerAdvice
public interface PingController {

    /** @return Uma response com código HTTP 200 e o body "pong" */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ping")
    public ResponseEntity<String> ping();

    /**
     * @param quantity quantidade de "pongs" que serão retornados
     * @return Uma response com código HTTP 200 e o body com uma lista de `quantity` pongs
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pings")
    public ResponseEntity<List<String>> pings(
            @Parameter(description = "quantity of pongs to be delivered") @RequestParam int quantity);
}
