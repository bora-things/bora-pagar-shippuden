package com.borathings.borapagar.friendRequest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendRequestCreateDto(@JsonProperty("to_id") Integer toUserId) {}
