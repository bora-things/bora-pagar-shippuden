package com.borathings.borapagar.task;

import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.component.repository.ComponentRepository;
import com.borathings.borapagar.student.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class ComponentFetchService {

    @Autowired
    @Qualifier("serviceRestClient")
    RestClient serviceRestClient;

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    ComponentMapper componentMapper;

    @Async
    public void fetchComponents() {
        List<String> curricularMatrixIdList = List.of("134044403", "133795010", "133797961", "133804382");
        curricularMatrixIdList.forEach(this::fetchComponents);
    }

    void fetchComponents(String curricularMatrixId) {
        List<ComponentDTO> componentsFetched = new ArrayList<>();
        boolean hasComponents = true;
        int offset = 0;
        while (hasComponents) {
            List<ComponentDTO> components = serviceRestClient
                    .get()
                    .uri("/curso/v1/componentes-curriculares?nivel=G&id-unidade=6069&limit=100&offset=" + offset + "&id-matriz-curricular=" + curricularMatrixId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ComponentDTO>>() {
                    });
            if (components != null) {
                if (components.size() < 100) {
                    hasComponents = false;
                }
                componentsFetched.addAll(components);
                offset += 100;
                System.out.println(componentsFetched.size());
            }
        }
        componentRepository.saveAll(componentsFetched.stream().map(componentMapper::toEntity).toList());
    }


}
