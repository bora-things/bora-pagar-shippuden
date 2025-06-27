package com.borathings.borapagar.component;

import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.component.repository.ComponentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ComponentService {

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    private ComponentFetchService componentFetchService;

    @Autowired
    ComponentMapper componentMapper;

    public Page<ComponentEntity> getAllComponentsPageable(Pageable pageable) {
        return componentRepository.findAll(pageable);
    }

    public List<ComponentEntity> findAllByCodeIn(List<String> codes) {
        return componentRepository.findAllByCodeIn(codes);
    }

    @Async
    public void fetchComponents() {
        List<Integer> curricularMatrixIdList = List.of(134044403, 133795010, 133797961, 133804382);

        curricularMatrixIdList.forEach(componentFetchService::fetchComponentsByMatrix);
    }

    public List<ComponentDTO> findSearchedComponents(String searched) {
        List<ComponentEntity> components = componentRepository.searchByNameOrCode(searched);
        return components.stream().map(componentMapper::toDto).toList();
    }

    public Optional<ComponentEntity> findByCode(String code) {
        return componentRepository.findFirstByCode(code);
    }
}
