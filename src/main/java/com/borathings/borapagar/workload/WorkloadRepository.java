package com.borathings.borapagar.workload;

import com.borathings.borapagar.core.AbstractRepository;
import com.borathings.borapagar.student.StudentEntity;

public interface WorkloadRepository extends AbstractRepository<WorkloadEntity> {

    void deleteAllByStudent(StudentEntity student);
}
