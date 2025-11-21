package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.core.persistence.AbstractRepository;
import com.borathings.borapagar.user.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    List<StudentSubjectInterestEntity> findAllByStudentIdAndDeletedAtIsNull(Long studentId);

    Optional<StudentSubjectInterestEntity> findBySubjectCodeAndStudentIdAndDeletedAtIsNull(
            String subjectCode, Long studentId);

    @Query(
            "SELECT ssi FROM StudentSubjectInterestEntity ssi JOIN FETCH ssi.student WHERE ssi.student.user.id IN :studentIds AND ssi.deletedAt IS NULL")
    List<StudentSubjectInterestEntity> findAllByStudentIdInAndDeletedAtIsNull(
            @Param("studentIds") List<Long> studentIds);

    @Query(
            """
    SELECT ssi FROM StudentSubjectInterestEntity ssi
    JOIN FETCH ssi.student
    WHERE ssi.student.user IN :users
    AND ssi.period = :period AND ssi.year = :year
    AND ssi.deletedAt IS NULL
""")
    List<StudentSubjectInterestEntity> findAllByUserInAndPeriodAndYear(
            @Param("users") Set<UserEntity> users, @Param("period") Integer period, @Param("year") Integer year);

    @Query(
            "SELECT ssi FROM StudentSubjectInterestEntity ssi JOIN FETCH ssi.student WHERE ssi.student.user.id IN :studentIds and ssi.subjectCode=:subjectCode")
    List<StudentSubjectInterestEntity> findAllBySubjectCodeAndStudentInAndDeletedAtIsNull(
            @Param("subjectCode") String subjectCode, @Param("studentIds") List<Long> studentIds);

    List<StudentSubjectInterestEntity> findAllByYearAndPeriodAndDeletedAtIsNull(Integer year, Integer period);
}
