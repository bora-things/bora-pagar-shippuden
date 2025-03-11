package com.borathings.borapagar.friendRequest.dto;

import com.borathings.borapagar.friendRequest.FriendRequestStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendRequestUpdateDto(
        @JsonProperty("request_id") Long requestId, @JsonProperty("status") FriendRequestStatus status) {}
