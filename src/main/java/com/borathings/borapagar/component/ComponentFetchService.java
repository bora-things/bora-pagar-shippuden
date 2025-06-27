package com.borathings.borapagar.component;

import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.component.repository.ComponentRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class ComponentFetchService {

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    ComponentMapper componentMapper;

    @Autowired
    @Qualifier("serviceRestClient")
    RestClient serviceRestClient;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void fetchComponentsByMatrix(Integer curricularMatrixId) {
        // Apaga os components da matriz curricular
        componentRepository.deleteByCurricularMatrixId(curricularMatrixId);
        entityManager.flush();

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

        // Salva os components convertidos em entidades
        componentRepository.saveAll(
                componentsFetched.stream().map(componentMapper::toEntity).toList());
    }
}
