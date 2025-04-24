package com.borathings.borapagar.component;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class ComponentService {

    @Autowired
    @Qualifier("userRestClient")
    private RestClient userRestClient;

    @Autowired
    private ComponentMapper componentMapper;

    public Map<String, ComponentResponseDTO> fetchComponents(List<ClassroomEntity> classroomEntities, String token) {

//        List<CompletableFuture<ComponentResponseDTO>> futures = classroomEntities.stream()
//                .map(classroom -> fetchComponentAsync(classroom.getComponentCode(),token))
//                .toList();
//
//        // Espera todas as futures completarem
//
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//
//        return futures.stream()
//                .map(CompletableFuture::join)
//                .collect(Collectors.toMap(
//                        ComponentResponseDTO::code,
//                        component -> component,
//                        (existing, replacement) -> existing // ou replacement se quiser o novo
//                ));
        return null;
    }

    @Async
    public CompletableFuture<ComponentResponseDTO> fetchComponentAsync(String code,String token) {
//            List<ComponentDTO> components = user
//                    .get()
//                    .uri("https://api.info.ufrn.br/curso/v1/componentes-curriculares?codigo=" + code)
//                    .header("Authorization", "Bearer " + token)
//                    .retrieve()
//                    .body(new ParameterizedTypeReference<List<ComponentDTO>>() {});
//
//        if (components != null) {
//            return CompletableFuture.completedFuture(componentMapper.toResponseDTO(components.getFirst()));
//        }
        return CompletableFuture.completedFuture(null);
    }

}
