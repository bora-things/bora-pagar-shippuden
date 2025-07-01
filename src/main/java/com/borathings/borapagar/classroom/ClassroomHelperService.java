package com.borathings.borapagar.classroom;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class ClassroomHelperService {

    @Autowired
    @Qualifier("serviceRestClient")
    RestClient serviceRestClient;

    @Autowired
    private ClassroomMapper classroomMapper;

    public List<ClassroomEntity> findClassroomsByComponentCode(String code) {

        List<ClassroomDTO> classroomDTOs = serviceRestClient
                .get()
                .uri("https://api.info.ufrn.br/turma/v1/turmas?codigo-componente=" + code)
                .attributes(clientRegistrationId("sigaa"))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ClassroomDTO>>() {});

        if (classroomDTOs != null && !classroomDTOs.isEmpty()) {
            return classroomDTOs.stream()
                    .map(classroomMapper::toEntity)
                    .toList();
        }

        return Collections.emptyList();
    }
}
