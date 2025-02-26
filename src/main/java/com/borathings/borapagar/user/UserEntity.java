package com.borathings.borapagar.user;

import com.borathings.borapagar.core.SoftDeletableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

/** UserEntity */
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserEntity extends SoftDeletableModel {
    @Column(nullable = false)
    @NotNull
    private String email;

    @Column(name = "person_name", nullable = false)
    @NotNull
    private String personName;

    @Column(nullable = false)
    @NotNull
    private String login;

    @Column(name = "user_id", nullable = false, unique = true)
    @NotNull
    @NaturalId
    private int userId;

    @Column(name = "institutional_id", nullable = false)
    @NotNull
    private Long institutionalId;

    @Column(nullable = false)
    @NotNull
    private String cpf;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "unit_id", nullable = false)
    @NotNull
    private int unitId;

    @Column(nullable = false)
    @NotNull
    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<UserEntity> friends = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity other = (UserEntity) o;
        return Objects.equals(getUserId(), other.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId());
    }


    public void addFriend(UserEntity friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
            friend.getFriends().add(this);
        }
    }

    public void removeFriend(UserEntity friend) {
        if (friends.contains(friend)) {
            friends.remove(friend);
            friend.getFriends().remove(this);
        }
    }

}
