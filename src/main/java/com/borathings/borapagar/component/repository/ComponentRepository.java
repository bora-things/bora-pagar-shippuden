package com.borathings.borapagar.component.repository;

import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.core.persistence.AbstractRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComponentRepository extends AbstractRepository<ComponentEntity> {

    List<ComponentEntity> findAllByCodeIn(List<String> codes);

    @Query("SELECT c FROM components c WHERE c.id IN "
            + "(SELECT MIN(c2.id) FROM components c2 WHERE c2.code IN :codes GROUP BY c2.code)")
    List<ComponentEntity> findDistinctByCodeIn(List<String> codes);

    Page<ComponentEntity> findAll(Pageable pageable);

    List<ComponentEntity> findAllByComponentIdIn(List<Integer> ids);

    @Query("SELECT c FROM components c " + "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searched, '%')) "
            + "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :searched, '%'))")
    List<ComponentEntity> searchByNameOrCode(@Param("searched") String searched);

    Optional<ComponentEntity> findFirstByCode(String code);

    @Modifying
    void deleteByCurricularMatrixId(@Param("curricularMatrixId") Integer curricularMatrixId);
}
