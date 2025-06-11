package com.borathings.borapagar.component.repository;

import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.core.AbstractRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComponentRepository extends AbstractRepository<ComponentEntity> {

    List<ComponentEntity> findAllByCodeIn(List<String> code);

    Page<ComponentEntity> findAll(Pageable pageable);

    @Query("SELECT c FROM components c " + "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searched, '%')) "
            + "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :searched, '%'))")
    List<ComponentEntity> searchByNameOrCode(@Param("searched") String searched);

    Optional<ComponentEntity> findFirstByCode(String code);



}
