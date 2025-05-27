package com.borathings.borapagar.component.repository;

import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.core.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComponentRepository extends AbstractRepository<ComponentEntity> {

    List<ComponentEntity> findAllByCodeIn(List<String> code);

    Page<ComponentEntity> findAll(Pageable pageable);

}
