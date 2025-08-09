package com.borathings.borapagar.student.takenComponent;

import com.borathings.borapagar.student.StudentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TakenComponentRepository extends JpaRepository<TakenComponentEntity, Long> {

    List<TakenComponentEntity> findAllByStudent(StudentEntity student);

    Optional<TakenComponentEntity> findByComponentId(Integer componentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TakenComponentEntity t WHERE t.student = :student")
    void deleteAllByStudent(@Param("student") StudentEntity student);
}
