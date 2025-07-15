package com.borathings.borapagar.component.repository;

import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.core.AbstractRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComponentRepository extends AbstractRepository<ComponentEntity> {

    @Query(
            value =
                    """
    SELECT DISTINCT ON (c.code) *
    FROM components c
    WHERE c.code IN (:codes)
    ORDER BY c.code, c.id DESC
""",
            nativeQuery = true)
    List<ComponentEntity> findAllByCodeIn(List<String> codes);

    Page<ComponentEntity> findAll(Pageable pageable);

    @Query("SELECT c FROM components c " + "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searched, '%')) "
            + "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :searched, '%'))")
    List<ComponentEntity> searchByNameOrCode(@Param("searched") String searched);

    Optional<ComponentEntity> findFirstByCode(String code);

    @Modifying
    void deleteByCurricularMatrixId(@Param("curricularMatrixId") Integer curricularMatrixId);

    @Query(value = 
        """
        SELECT *, embedding <=> ? as distance 
        FROM components c ORDER BY embedding <=> ?1 LIMIT ?2;
        JOIN components_embeddings ce on ce.component_id = c.id  
        """, nativeQuery = true)
    List<ComponentEntity> topKComponents(List<Integer> embedding, int k);
}
