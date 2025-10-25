package com.borathings.borapagar.enrollmentRank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EnrollmentRankRepository extends JpaRepository<EnrollmentRankEntity, Long> {

    void deleteAllByClassIdIn(List<Long> classIds);

    List<EnrollmentRankEntity> findAllByStudentIdAndYearAndPeriod(Long studentId, Integer year, Integer period);

    @Query("SELECT er.classId, er.priorityTypeId, COUNT(er) " +
            "FROM EnrollmentRankEntity er " +
            "WHERE er.classId IN :classIds " +
            "GROUP BY er.classId, er.priorityTypeId")
    List<Object[]> countRanksForClasses(@Param("classIds") Set<Long> classIds);
}
