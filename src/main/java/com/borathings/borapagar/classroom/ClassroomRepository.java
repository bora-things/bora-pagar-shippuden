package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.AbstractRepository;
import com.borathings.borapagar.student.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends AbstractRepository<ClassroomEntity> {

    void deleteAllByStudent(StudentEntity student);

    List<ClassroomEntity> findAllByStudent(StudentEntity student);

}
