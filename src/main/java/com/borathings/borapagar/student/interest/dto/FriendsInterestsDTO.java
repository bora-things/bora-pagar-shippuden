package com.borathings.borapagar.student.interest.dto;

import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import java.util.List;

public record FriendsInterestsDTO(
        List<ComponentResponseDTO> components, List<StudentFriendInterestDTO> friendsInterests) {}
