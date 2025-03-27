package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.core.AbstractRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StudentSubjectInterestRepository extends AbstractRepository<StudentSubjectInterestEntity> {

    @Modifying
    @Transactional
    @Query(
            "DELETE FROM StudentSubjectInterestEntity s WHERE s.sigaaSubjectId = :sigaaSubjectId AND s.student.id = :studentId")
    void deleteBySigaaSubjectIdAndStudentId(
            @Param("sigaaSubjectId") int sigaaSubjectId, @Param("studentId") Long studentId);

    List<StudentSubjectInterestEntity> findAllByStudentId(Long studentId);
}
