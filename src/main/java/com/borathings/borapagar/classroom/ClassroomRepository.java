package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.AbstractRepository;
import com.borathings.borapagar.student.StudentEntity;
import java.util.List;

public interface ClassroomRepository extends AbstractRepository<ClassroomEntity> {

    void deleteAllByStudent(StudentEntity student);

    List<ClassroomEntity> findAllByStudent(StudentEntity student);
}
