package com.borathings.borapagar.student;

import com.borathings.borapagar.core.AbstractRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.borathings.borapagar.user.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends AbstractRepository<StudentEntity> {
    @Query("SELECT s FROM students s WHERE s.user.userId = :userId")
    Optional<StudentEntity> findByUserId(int userId);

    List<StudentEntity> findAllByUserIn(Collection<UserEntity> users);


}
