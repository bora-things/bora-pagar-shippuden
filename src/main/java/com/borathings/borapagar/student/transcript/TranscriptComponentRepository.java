package com.borathings.borapagar.student.transcript;

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
public interface TranscriptComponentRepository extends JpaRepository<TranscriptComponentEntity, Long> {

    List<TranscriptComponentEntity> findAllByStudent(StudentEntity student);

    Optional<TranscriptComponentEntity> findByComponentId(Integer componentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TranscriptComponentEntity t WHERE t.student = :student")
    void deleteByStudent(@Param("student") StudentEntity student);
}
