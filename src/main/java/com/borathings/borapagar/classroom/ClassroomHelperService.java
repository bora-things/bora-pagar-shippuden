package com.borathings.borapagar.classroom;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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
                .uri("/turma/v1/turmas?codigo-componente=" + code)
                .attributes(clientRegistrationId("sigaa"))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ClassroomDTO>>() {});

        if (classroomDTOs != null && !classroomDTOs.isEmpty()) {
            return classroomDTOs.stream().map(classroomMapper::toEntity).toList();
        }

        return Collections.emptyList();
    }
}
