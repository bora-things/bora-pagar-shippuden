package com.borathings.borapagar.friendRequest.impl;

import com.borathings.borapagar.friendRequest.FriendRequestController;
import com.borathings.borapagar.friendRequest.FriendRequestService;
import com.borathings.borapagar.friendRequest.dto.FriendRequestCreateDto;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUpdateDto;
import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendRequestControllerImpl implements FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Override
    public ResponseEntity<Void> createFriendRequest(
            Authentication currentUser, @RequestBody FriendRequestCreateDto friendRequestCreateDto) {
        String fromUserLogin = currentUser.getName();
        Integer toUserId = friendRequestCreateDto.toUserId();
        friendRequestService.createFriendRequest(fromUserLogin, toUserId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> updateFriendRequest(
            Authentication authentication, @RequestBody FriendRequestUpdateDto friendRequestUpdateDto) {
        Long requestId = friendRequestUpdateDto.requestId();
        friendRequestService.updateFriendRequest(requestId, friendRequestUpdateDto.status());
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @Override
    public ResponseEntity<Void> deleteFriendRequest(Authentication authentication, Long friendRequestId) {
        friendRequestService.deleteFriendRequest(friendRequestId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<FriendRequestResponseDto>> getFriendRequests(Authentication authentication) {
        String toUserLogin = authentication.getName();
        List<FriendRequestResponseDto> requests = friendRequestService.findAllByToUserId(toUserLogin);
        return ResponseEntity.status(HttpStatus.OK).body(requests);
    }
}
