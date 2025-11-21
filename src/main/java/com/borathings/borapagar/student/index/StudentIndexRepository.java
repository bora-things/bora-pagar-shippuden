package com.borathings.borapagar.student.index;

import com.borathings.borapagar.core.persistence.AbstractRepository;
import com.borathings.borapagar.student.StudentEntity;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentIndexRepository extends AbstractRepository<StudentIndexEntity> {

    void deleteAllByStudent(StudentEntity student);

    List<StudentIndexEntity> findAllByNameEqualsAndStudentIdIn(String name, Set<Long> studentIds);
}
