package com.borathings.borapagar.core;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/** SoftDeletableRepository */
@NoRepositoryBean
public interface SoftDeletableRepository<M extends SoftDeletableModel>
        extends AbstractRepository<M> {

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = ?1")
    public void softDeleteById(Long id);

    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL")
    public List<M> findAll();
}
