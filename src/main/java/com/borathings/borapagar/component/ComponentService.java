package com.borathings.borapagar.component;

import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.component.repository.ComponentRepository;
import com.borathings.borapagar.subject.dto.ComponentDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ComponentService {

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
                    .uri("/curso/v1/componentes-curriculares?nivel=G&limit=100&offset=" + offset
                            + "&id-matriz-curricular=" + curricularMatrixId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ComponentDTO>>() {});
            if (components != null) {
                if (components.size() < 100) {
                    hasComponents = false;
                }
                componentsFetched.addAll(components);
                offset += 100;
            }
        }
        componentRepository.saveAll(
                componentsFetched.stream().map(componentMapper::toEntity).toList());
    }
}
