package com.borathings.borapagar.component;

import com.borathings.borapagar.component.dto.ComponentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class ComponentService {

    @Autowired
    RestClient restClient;

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    ComponentMapper componentMapper;

    @Value("${sigaa.SERVICE_TOKEN}")
    private String serviceToken;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public CompletableFuture<Void> fetchComponents() {
        logger.info("Iniciando requisição para buscar componentes");

        try {


            List<ComponentDto> components = restClient
                    .get()
                    .uri("/curso/v1/componentes-curriculares")
                    .header("Authorization", "Bearer " + serviceToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ComponentDto>>() {
                    });

            logger.info("Requisição concluída");

            List<ComponentEntity> componentEntities = components.stream().map(componentMapper::toEntity).toList();
            componentRepository.saveAll(componentEntities);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            logger.error("Erro ao buscar componentes", e);
            return CompletableFuture.completedFuture(null);
        }
    }
}
