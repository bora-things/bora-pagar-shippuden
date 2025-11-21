package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.core.persistence.SoftDeletableModel;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity(name = "friend_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class FriendRequestEntity extends SoftDeletableModel {

    @ManyToOne
    @JoinColumn(name = "to_id")
    private UserEntity toUser;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private UserEntity fromUser;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "friend_request_status")
    private FriendRequestStatus status;
}
