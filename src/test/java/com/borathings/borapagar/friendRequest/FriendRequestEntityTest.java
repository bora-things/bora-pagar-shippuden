package com.borathings.borapagar.friendRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FriendRequestEntityTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    private UserEntity fromUser;
    private UserEntity toUser;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        fromUser = UserEntity.builder()
                .personName("User From")
                .institutionalId(12345L)
                .login("UserFrom")
                .cpf("12345")
                .email("fromuser@example.com")
                .build();
        toUser = UserEntity.builder()
                .institutionalId(54321L)
                .login("UserTo")
                .cpf("54321")
                .personName("User To")
                .email("touser@example.com")
                .build();
    }

    @Test
    public void testCreateFriendRequest() {
        when(userRepository.save(fromUser)).thenReturn(fromUser);
        when(userRepository.save(toUser)).thenReturn(toUser);

        FriendRequestEntity friendRequest = FriendRequestEntity.builder()
                .id(1L)
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);

        FriendRequestEntity savedRequest = friendRequestRepository.save(friendRequest);

        assertNotNull(savedRequest.getId());
        assertEquals(fromUser.getId(), savedRequest.getFromUser().getId());
        assertEquals(toUser.getId(), savedRequest.getToUser().getId());
        assertEquals(FriendRequestStatus.PENDING, savedRequest.getStatus());

        verify(friendRequestRepository, times(1)).save(friendRequest);
    }

    @Test
    public void testFriendRequestStatusUpdate() {
        FriendRequestEntity friendRequest = FriendRequestEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        when(friendRequestRepository.save(friendRequest)).thenReturn(friendRequest);

        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
        FriendRequestEntity updatedRequest = friendRequestRepository.save(friendRequest);

        assertEquals(FriendRequestStatus.ACCEPTED, updatedRequest.getStatus());

        verify(friendRequestRepository, times(1)).save(friendRequest);
    }
}
