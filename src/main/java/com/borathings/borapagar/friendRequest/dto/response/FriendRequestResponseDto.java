package com.borathings.borapagar.friendRequest.dto.response;

import com.borathings.borapagar.friendRequest.FriendRequestStatus;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestResponseDto {
    private Long id;
    private FriendRequestUserDto user;
}
