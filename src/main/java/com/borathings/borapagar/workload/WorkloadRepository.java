package com.borathings.borapagar.workload;

import com.borathings.borapagar.core.persistence.AbstractRepository;
import com.borathings.borapagar.student.StudentEntity;
import java.util.Optional;

public interface WorkloadRepository extends AbstractRepository<WorkloadEntity> {

    void deleteAllByStudent(StudentEntity student);

    Optional<WorkloadEntity> findByStudent(StudentEntity student);
}
