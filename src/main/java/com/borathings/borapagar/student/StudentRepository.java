package com.borathings.borapagar.student;

import com.borathings.borapagar.core.persistence.AbstractRepository;
import com.borathings.borapagar.user.UserEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends AbstractRepository<StudentEntity> {
    @Query("SELECT s FROM students s WHERE s.user.userId = :userId")
    Optional<StudentEntity> findByUserId(int userId);

    @Query("SELECT s FROM students s WHERE s.user.login = :userLogin")
    Optional<StudentEntity> findByUserLogin(String userLogin);

    List<StudentEntity> findAllByUserIn(Collection<UserEntity> users);

    @Query("SELECT s FROM students s LEFT JOIN FETCH s.classrooms WHERE s.id = :id")
    Optional<StudentEntity> findByIdWithClassrooms(@Param("id") Long id);

    Page<StudentEntity> findByStudentNameContainingIgnoreCaseAndIdNot(
            String studentName, Long studentIdToExclude, Pageable pageable);

    @Query("SELECT friendStudent FROM students currentUser " + "JOIN currentUser.user.friends friendUser "
            + "JOIN students friendStudent ON friendStudent.user = friendUser "
            + "WHERE currentUser.studentId = :currentStudentId "
            + "AND LOWER(friendStudent.studentName) LIKE LOWER(CONCAT('%', :studentName, '%'))")
    Page<StudentEntity> findFriendsByName(
            @Param("studentName") String studentName,
            @Param("currentStudentId") Long currentStudentId,
            Pageable pageable);

    @Query("SELECT friendStudent FROM students currentUser " + "JOIN currentUser.user.friends friendUser "
            + "JOIN students friendStudent ON friendStudent.user = friendUser "
            + "WHERE currentUser.studentId = :currentStudentId ")
    Page<StudentEntity> findFriends(@Param("currentStudentId") Long currentStudentId, Pageable pageable);

    @Query("SELECT s3 FROM students s3 WHERE s3.user IN (" + "  SELECT u3 FROM students s1 "
            + "  JOIN s1.user.friends u2 "
            + "  JOIN u2.friends u3 "
            + "  WHERE s1.studentId = :currentStudentId"
            + ") "
            + "AND s3.studentId != :currentStudentId "
            + "AND s3.user NOT IN ("
            + "  SELECT directFriends FROM students su "
            + "  JOIN su.user.friends directFriends "
            + "  WHERE su.studentId = :currentStudentId"
            + ")")
    Page<StudentEntity> findFriendsOfFriends(@Param("currentStudentId") Long currentStudentId, Pageable pageable);
}
