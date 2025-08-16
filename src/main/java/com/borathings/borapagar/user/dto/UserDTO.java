package com.borathings.borapagar.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record UserDTO(
        String login,
        @JsonProperty("nome-pessoa") String personName,
        @JsonProperty("id-usuario") int userId,
        @JsonProperty("id-institucional") Long institutionalId,
        @JsonProperty("url-foto") String imageUrl,
        boolean deleted) {
    public static UserDTO fromSigaaUser(OAuth2User user) {
        return new UserDTO(
                user.getName(),
                user.getAttribute("nome-pessoa"),
                user.getAttribute("id-usuario"),
                user.getAttribute("id-institucional"),
                user.getAttribute("url-foto"),
                false);
    }
}
