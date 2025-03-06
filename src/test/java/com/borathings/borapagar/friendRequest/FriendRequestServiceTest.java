package com.borathings.borapagar.friendRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private FriendRequestMapper friendRequestMapper;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private FriendRequestService friendRequestService;

    private UserEntity fromUser;
    private UserEntity toUser;
    private FriendRequestEntity friendRequest;

    @BeforeEach
    void setUp() {
        fromUser = new UserEntity();
        fromUser.setId(1L);
        fromUser.setLogin("fromUser");

        toUser = new UserEntity();
        toUser.setId(2L);
        toUser.setLogin("toUser");

        friendRequest = new FriendRequestEntity();
        friendRequest.setId(1L);
        friendRequest.setFromUser(fromUser);
        friendRequest.setToUser(toUser);
        friendRequest.setStatus(FriendRequestStatus.PENDING);
    }

    @Test
    void testFindAllByToUserId() {
        when(userService.findByLoginOrError("toUser")).thenReturn(toUser);
        when(friendRequestRepository.findAllByToUser(toUser)).thenReturn(List.of(friendRequest));
        when(studentService.findAllStudentsById(List.of(fromUser))).thenReturn(List.of());
        when(friendRequestMapper.toFriendRequestResponseDto(any(), any())).thenReturn(new FriendRequestResponseDto());

        List<FriendRequestResponseDto> result = friendRequestService.findAllByToUserId("toUser");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateFriendRequest() {
        when(userService.findByLoginOrError("fromUser")).thenReturn(fromUser);
        when(userService.findByIdUserOrError(2)).thenReturn(toUser);
        when(friendRequestRepository.save(any())).thenReturn(friendRequest);

        assertDoesNotThrow(() -> friendRequestService.createFriendRequest("fromUser", 2));
        verify(friendRequestRepository, times(1)).save(any());
    }

    @Test
    void testUpdateFriendRequest() {
        when(friendRequestRepository.findById(1L)).thenReturn(Optional.of(friendRequest));

        friendRequestService.updateFriendRequest(1L, FriendRequestStatus.ACCEPTED);
        assertEquals(FriendRequestStatus.ACCEPTED, friendRequest.getStatus());
        verify(friendRequestRepository, times(1)).save(friendRequest);
        verify(userService, times(1)).createFriendship(toUser, fromUser);
    }

    @Test
    void testUpdateFriendRequest_failure_entityNotFound() {
        when(friendRequestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> friendRequestService.updateFriendRequest(1L, FriendRequestStatus.ACCEPTED));
    }

    @Test
    void testDeleteFriendRequest_success() {
        when(friendRequestRepository.findById(1L)).thenReturn(Optional.of(friendRequest));

        friendRequestService.deleteFriendRequest(1L);
        verify(friendRequestRepository, times(1)).softDeleteById(1L);
    }

    @Test
    void testDeleteFriendRequest_failure_entityNotFound() {
        when(friendRequestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> friendRequestService.deleteFriendRequest(1L));
    }
}
