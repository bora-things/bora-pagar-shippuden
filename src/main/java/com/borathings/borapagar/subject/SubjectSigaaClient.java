package com.borathings.borapagar.subject;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.subject.dto.ComponentDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SubjectSigaaClient {

    private final String componentUrl = "/curso/v1/componentes-curriculares/";

    private RestClient restClient;

    public SubjectSigaaClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ComponentDTO getComponentById(int id) {
        return restClient
                .get()
                .uri(componentUrl + id)
                .attributes(clientRegistrationId("sigaa"))
                .retrieve()
                .body(new ParameterizedTypeReference<ComponentDTO>() {});
    }
}
