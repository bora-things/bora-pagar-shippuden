package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.core.SoftDeletableModel;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "friend_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class FriendRequestEntity extends SoftDeletableModel {

    @ManyToOne
    @JoinColumn(name = "to_id")
    private UserEntity toUser;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private UserEntity fromUser;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;
}
