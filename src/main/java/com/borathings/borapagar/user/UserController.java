package com.borathings.borapagar.user;

import com.borathings.borapagar.friendRequest.dto.FriendRequestUserDto;
import com.borathings.borapagar.user.dto.response.UserFriendResponseDto;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/** UserController */
@RequestMapping("/users")
public interface UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication);


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/friends")
    public ResponseEntity<List<UserFriendResponseDto>> getUserFriends(Authentication authentication);

}
