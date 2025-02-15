package com.borathings.borapagar.student;

import com.borathings.borapagar.core.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends AbstractRepository<StudentEntity> {
    @Query("SELECT COUNT(s) > 0 FROM students s WHERE s.user.userId = :userId")
    boolean existsByUserId(int userId);
}
