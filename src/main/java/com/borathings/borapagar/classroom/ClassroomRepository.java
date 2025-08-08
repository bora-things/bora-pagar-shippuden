package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.persistence.AbstractRepository;
import java.util.Optional;

public interface ClassroomRepository extends AbstractRepository<ClassroomEntity> {

    Optional<ClassroomEntity> findByClassroomId(long classroomId);
}
