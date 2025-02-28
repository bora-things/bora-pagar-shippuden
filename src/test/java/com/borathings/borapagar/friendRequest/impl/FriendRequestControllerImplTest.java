package com.borathings.borapagar.friendRequest.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.friendRequest.FriendRequestService;
import com.borathings.borapagar.friendRequest.FriendRequestStatus;
import com.borathings.borapagar.friendRequest.dto.FriendRequestCreateDto;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUpdateDto;
import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

class FriendRequestControllerImplTest {

    @InjectMocks
    private FriendRequestControllerImpl friendRequestController;

    @Mock
    private FriendRequestService friendRequestService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFriendRequest_Success() {
        FriendRequestCreateDto requestDto = new FriendRequestCreateDto(1);
        when(authentication.getName()).thenReturn("user123");
        when(friendRequestService.createFriendRequest("user123", 1)).thenReturn(true);

        ResponseEntity<Void> response = friendRequestController.createFriendRequest(authentication, requestDto);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateFriendRequest_Failure() {
        FriendRequestCreateDto requestDto = new FriendRequestCreateDto(1);
        when(authentication.getName()).thenReturn("user123");
        when(friendRequestService.createFriendRequest("user123", 1)).thenReturn(false);

        ResponseEntity<Void> response = friendRequestController.createFriendRequest(authentication, requestDto);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testUpdateFriendRequest_Success() {
        FriendRequestUpdateDto requestDto = new FriendRequestUpdateDto(2, FriendRequestStatus.ACCEPTED);
        when(authentication.getName()).thenReturn("user123");
        when(friendRequestService.updateFriendRequest("user123", 2, FriendRequestStatus.ACCEPTED))
                .thenReturn(true);

        ResponseEntity<Void> response = friendRequestController.updateFriendRequest(authentication, requestDto);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateFriendRequest_Failure() {
        FriendRequestUpdateDto requestDto = new FriendRequestUpdateDto(2, FriendRequestStatus.ACCEPTED);
        when(authentication.getName()).thenReturn("user123");
        when(friendRequestService.updateFriendRequest("user123", 2, FriendRequestStatus.ACCEPTED))
                .thenReturn(false);

        ResponseEntity<Void> response = friendRequestController.updateFriendRequest(authentication, requestDto);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testGetFriendRequests() {
        when(authentication.getName()).thenReturn("user123");
        when(friendRequestService.findAllByToUserId("user123")).thenReturn(Collections.emptyList());

        ResponseEntity<List<FriendRequestResponseDto>> response =
                friendRequestController.getFriendRequests(authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
