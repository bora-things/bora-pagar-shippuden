package com.borathings.borapagar.component;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.component.dto.ComponentDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class SubjectSigaaClient {

    private final String componentUrl = "curso/v1/componentes-curriculares?codigo=";

    @Qualifier("userRestClient")
    private RestClient userRestClient;

    public SubjectSigaaClient(RestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    public ComponentDTO getComponentByCode(String code) {
        return userRestClient
                .get()
                .uri(componentUrl + code)
                .attributes(clientRegistrationId("sigaa"))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ComponentDTO>>() {}).getFirst();
    }


}
