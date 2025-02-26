package com.borathings.borapagar.subject;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.subject.dto.CurricularComponentDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SubjectService {

    @Autowired
    RestClient restClient;

    public CurricularComponentDTO getSubjectByCode(String code) {
        try {
            List<CurricularComponentDTO> componentDTOs = restClient
                    .get()
                    .uri("/curso/v1/componentes-curriculares?codigo={code}", code)
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CurricularComponentDTO>>() {});

            return componentDTOs.getFirst();
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException("Não foi encontrada disciplina com código " + code, e);
        }
    }
}
