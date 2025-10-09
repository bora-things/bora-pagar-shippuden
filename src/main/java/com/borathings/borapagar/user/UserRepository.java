package com.borathings.borapagar.user;

import com.borathings.borapagar.core.persistence.SoftDeletableRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** UserRepository */
@Repository
public interface UserRepository extends SoftDeletableRepository<UserEntity> {
    Optional<UserEntity> findByUserId(int idUser);

    Optional<UserEntity> findByLogin(String login);

    @Query("SELECT COUNT(f) > 0 FROM users u JOIN u.friends f WHERE u.id = :userId AND f.id = :friendId")
    boolean areFriends(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query("SELECT f.id FROM users u JOIN u.friends f WHERE u.id = :userId AND f.id IN :targetUserIds")
    Set<Long> findFriendIdsFromTargetList(
            @Param("userId") Long userId, @Param("targetUserIds") List<Long> targetUserIds);
}
