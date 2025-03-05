package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.core.AbstractRepository;
import com.borathings.borapagar.core.SoftDeletableRepository;
import com.borathings.borapagar.user.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends SoftDeletableRepository<FriendRequestEntity> {


    @Query("SELECT f FROM friend_requests f WHERE f.toUser = :toUser AND f.deletedAt IS NULL")
    List<FriendRequestEntity> findAllByToUser(@Param("toUser") UserEntity toUser);

    @Query("SELECT f FROM friend_requests f WHERE f.fromUser = :fromUser AND f.toUser = :toUser AND f.deletedAt IS NULL")
    Optional<FriendRequestEntity> findByFromUserAndToUser(@Param("fromUser") UserEntity fromUser, @Param("toUser") UserEntity toUser);
}
