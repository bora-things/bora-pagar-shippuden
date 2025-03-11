package com.borathings.borapagar.user.impl;

import com.borathings.borapagar.user.UserController;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import com.borathings.borapagar.user.UserService;
import com.borathings.borapagar.user.dto.response.UserFriendResponseDto;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

/** UserControllerImpl */
@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication currentUser) {
        UserEntity loggedUser = userService.findByLoginOrError(currentUser.getName());
        return ResponseEntity.ok(userMapper.toUserResponseDTO(loggedUser));
    }

    @Override
    public ResponseEntity<List<UserFriendResponseDto>> getUserFriends(Authentication authentication) {
        List<UserFriendResponseDto> friends = userService.getFriends(authentication);
        return ResponseEntity.ok(friends);
    }

    @Override
    public ResponseEntity<Void> deleteUserFriends(Authentication authentication, Long friendId) {
        userService.removeFriend(authentication, friendId);
        return ResponseEntity.noContent().build();
    }
}
