package com.borathings.borapagar.friendRequest;

import com.borathings.borapagar.friendRequest.dto.FriendRequestCreateDto;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUpdateDto;
import com.borathings.borapagar.friendRequest.dto.response.FriendRequestResponseDto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/friend-requests")
public interface FriendRequestController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public ResponseEntity<List<FriendRequestResponseDto>> getFriendRequests(Authentication authentication);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public ResponseEntity<Void> createFriendRequest(
            Authentication authentication, @RequestBody FriendRequestCreateDto friendRequestCreateDto);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/update")
    public ResponseEntity<Void> updateFriendRequest(
            Authentication authentication, @RequestBody FriendRequestUpdateDto friendRequestUpdateDto);

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFriendRequest(Authentication authentication,@RequestParam("id") Long friendRequestId);
}
