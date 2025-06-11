package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.core.AbstractRepository;
import java.util.List;
import java.util.Optional;
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
            "DELETE FROM StudentSubjectInterestEntity s WHERE s.subjectCode = :subjectCode AND s.student.id = :studentId")
    void deleteBySigaaSubjectIdAndStudentId(
            @Param("subjectCode") String subjectCode, @Param("studentId") Long studentId);

    List<StudentSubjectInterestEntity> findAllByStudentId(Long studentId);

    Optional<StudentSubjectInterestEntity> findBySubjectCodeAndStudentId(String subjectCode, Long studentId);

    @Query("SELECT ssi FROM StudentSubjectInterestEntity ssi JOIN FETCH ssi.student WHERE ssi.student.id IN :studentIds")
    List<StudentSubjectInterestEntity> findAllByStudentIdIn(@Param("studentIds") List<Long> studentIds);
}
