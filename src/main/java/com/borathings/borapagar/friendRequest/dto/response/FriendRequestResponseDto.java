package com.borathings.borapagar.friendRequest.dto.response;

import com.borathings.borapagar.friendRequest.FriendRequestStatus;
import com.borathings.borapagar.friendRequest.dto.FriendRequestUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestResponseDto{
    private FriendRequestUserDto fromUser;
    private FriendRequestStatus status;
    @JsonProperty("create_date")
    private Date createDate;


}
