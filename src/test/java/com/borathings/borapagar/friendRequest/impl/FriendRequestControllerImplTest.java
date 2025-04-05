package com.borathings.borapagar.friendRequest.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.friendRequest.FriendRequestService;
import com.borathings.borapagar.friendRequest.FriendRequestStatus;
import com.borathings.borapagar.friendRequest.dto.FriendRequestCreateDto;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUpdateDto;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUserDto;
import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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
        doNothing().when(friendRequestService).createFriendRequest("user123", 1);

        ResponseEntity<Void> response = friendRequestController.createFriendRequest(authentication, requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateFriendRequest_Failure() {
        FriendRequestCreateDto requestDto = new FriendRequestCreateDto(1);

        when(authentication.getName()).thenReturn("user123");
        doThrow(new RuntimeException("Erro ao criar solicitação"))
                .when(friendRequestService)
                .createFriendRequest("user123", 1);

        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> friendRequestController.createFriendRequest(authentication, requestDto));

        assertEquals("Erro ao criar solicitação", exception.getMessage());
    }

    @Test
    void testUpdateFriendRequest_Success() {
        FriendRequestUpdateDto requestDto = new FriendRequestUpdateDto(1L, FriendRequestStatus.ACCEPTED);

        when(authentication.getName()).thenReturn("user123");
        doNothing().when(friendRequestService).updateFriendRequest(1L, FriendRequestStatus.ACCEPTED);

        ResponseEntity<Void> response = friendRequestController.updateFriendRequest(authentication, requestDto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateFriendRequest_Failure() {
        FriendRequestUpdateDto requestDto = new FriendRequestUpdateDto(1L, FriendRequestStatus.PENDING);

        when(authentication.getName()).thenReturn("user123");
        doThrow(new EntityNotFoundException("Pedido com ID:" + 1L + " não encontrado!"))
                .when(friendRequestService)
                .updateFriendRequest(1L, FriendRequestStatus.PENDING);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> friendRequestController.updateFriendRequest(authentication, requestDto));

        assertEquals("Pedido com ID:" + 1L + " não encontrado!", exception.getMessage());
    }

    @Test
    void testDeleteFriendRequest_Success() {
        when(authentication.getName()).thenReturn("user123");
        doNothing().when(friendRequestService).deleteFriendRequest(1L);

        ResponseEntity<Void> response = friendRequestController.deleteFriendRequest(authentication, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteFriendRequest_Failure() {
        when(authentication.getName()).thenReturn("user123");
        doThrow(new RuntimeException("Pedido com ID: 1 não encontrado!"))
                .when(friendRequestService)
                .deleteFriendRequest(1L);

        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> friendRequestController.deleteFriendRequest(authentication, 1L));

        assertEquals("Pedido com ID: 1 não encontrado!", exception.getMessage());
    }

    @Test
    void testGetFriendRequests_Success() {
        List<FriendRequestResponseDto> requests = Collections.singletonList(new FriendRequestResponseDto(
                new FriendRequestUserDto("PersonName", "imageUrl", "courseName", 1),
                FriendRequestStatus.PENDING,
                Date.from(Instant.now())));

        when(authentication.getName()).thenReturn("user123");
        when(friendRequestService.findAllByToUserIdWithStatus("user123", null)).thenReturn(requests);

        ResponseEntity<List<FriendRequestResponseDto>> response =
                friendRequestController.getFriendRequests(authentication, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetFriendRequests_NoRequests() {
        when(authentication.getName()).thenReturn("user123");
        when(friendRequestService.findAllByToUserIdWithStatus("user123", Optional.empty()))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<FriendRequestResponseDto>> response =
                friendRequestController.getFriendRequests(authentication, Optional.empty());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
