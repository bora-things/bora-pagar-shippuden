package com.borathings.borapagar.friendRequest.dto;

import com.borathings.borapagar.friendRequest.FriendRequestStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendRequestUpdateDto(
        @JsonProperty("from_id") Integer fromUserId, @JsonProperty("status") FriendRequestStatus status) {}
