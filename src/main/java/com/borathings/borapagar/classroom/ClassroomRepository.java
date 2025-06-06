package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.AbstractRepository;
import com.borathings.borapagar.student.StudentEntity;
import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends AbstractRepository<ClassroomEntity> {

    Optional<ClassroomEntity> findByClassroomId(long classroomId);
}
