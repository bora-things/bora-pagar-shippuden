package com.borathings.borapagar.student;

import com.borathings.borapagar.core.AbstractRepository;
import com.borathings.borapagar.user.UserEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends AbstractRepository<StudentEntity> {
    @Query("SELECT s FROM students s WHERE s.user.userId = :userId")
    Optional<StudentEntity> findByUserId(int userId);

    @Query("SELECT s FROM students s WHERE s.user.login = :userLogin")
    Optional<StudentEntity> findByUserLogin(String userLogin);

    List<StudentEntity> findAllByUserIn(Collection<UserEntity> users);
}
