package com.borathings.borapagar.user;

import com.borathings.borapagar.user.dto.response.UserFriendResponseDto;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** UserController */
@RequestMapping("/users")
public interface UserController {

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id);

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me/friends")
    public ResponseEntity<List<UserFriendResponseDto>> getUserFriends(Authentication authentication);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("me/friends/delete")
    public ResponseEntity<Void> deleteUserFriends(Authentication authentication, @RequestParam("id") Long friendId);
}
