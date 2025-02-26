package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.core.AbstractRepository;
import com.borathings.borapagar.user.UserEntity;
import org.apache.catalina.User;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends AbstractRepository<FriendRequestEntity> {

    List<FriendRequestEntity> findAllByToUser(UserEntity toUser);
    List<FriendRequestEntity> findAllByFromUser(UserEntity fromUser);
    Optional<FriendRequestEntity> findByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);
}
