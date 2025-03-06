package com.borathings.borapagar.friendRequest;

import static org.junit.jupiter.api.Assertions.*;

import com.borathings.borapagar.user.UserEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class FriendRequestEntityTest {

    @Test
    public void testCreateFriendRequestEntity() {
        UserEntity fromUser = UserEntity.builder()
                .id(1L)
                .personName("User From")
                .login("userfrom")
                .email("userfrom@example.com")
                .build();

        UserEntity toUser = UserEntity.builder()
                .id(2L)
                .personName("User To")
                .login("userto")
                .email("userto@example.com")
                .build();

        FriendRequestEntity friendRequest = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        assertNotNull(friendRequest);
        assertEquals(fromUser, friendRequest.getFromUser());
        assertEquals(toUser, friendRequest.getToUser());
        assertEquals(FriendRequestStatus.PENDING, friendRequest.getStatus());
    }

    @Test
    public void testEqualsAndHashCode() {
        UserEntity fromUser = UserEntity.builder()
                .id(1L)
                .personName("User From")
                .login("userfrom")
                .email("userfrom@example.com")
                .build();

        UserEntity toUser = UserEntity.builder()
                .id(2L)
                .personName("User To")
                .login("userto")
                .email("userto@example.com")
                .build();

        FriendRequestEntity friendRequest1 = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        FriendRequestEntity friendRequest2 = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        assertEquals(friendRequest1, friendRequest2);
        assertEquals(friendRequest1.hashCode(), friendRequest2.hashCode());
    }

    @Test
    public void testStatusUpdate() {
        UserEntity fromUser = UserEntity.builder()
                .id(1L)
                .personName("User From")
                .login("userfrom")
                .email("userfrom@example.com")
                .build();

        UserEntity toUser = UserEntity.builder()
                .id(2L)
                .personName("User To")
                .login("userto")
                .email("userto@example.com")
                .build();

        FriendRequestEntity friendRequest = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        assertEquals(FriendRequestStatus.PENDING, friendRequest.getStatus());

        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);

        assertEquals(FriendRequestStatus.ACCEPTED, friendRequest.getStatus());
    }

    @Test
    public void testSoftDelete() {
        UserEntity fromUser = UserEntity.builder()
                .id(1L)
                .personName("User From")
                .login("userfrom")
                .email("userfrom@example.com")
                .build();

        UserEntity toUser = UserEntity.builder()
                .id(2L)
                .personName("User To")
                .login("userto")
                .email("userto@example.com")
                .build();

        FriendRequestEntity friendRequest = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        assertNull(friendRequest.getDeletedAt());

        friendRequest.setDeletedAt(LocalDateTime.now());

        assertNotNull(friendRequest.getDeletedAt());
    }
}
