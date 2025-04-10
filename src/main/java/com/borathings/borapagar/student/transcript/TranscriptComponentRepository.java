package com.borathings.borapagar.student.transcript;

import com.borathings.borapagar.student.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptComponentRepository extends JpaRepository<TranscriptComponentEntity, Long> {

    void deleteByStudent(StudentEntity student);

}
