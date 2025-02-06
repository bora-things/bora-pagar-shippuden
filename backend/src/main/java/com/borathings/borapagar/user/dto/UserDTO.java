package com.borathings.borapagar.user.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

public record UserDTO(
        String login,
        String email,
        String name,
        int idUsuario,
        Long idDiscente,
        Long idInstitucional,
        Long cpf,
        String imageUrl,
        boolean deleted) {
    public static UserDTO fromSigaaUser(OAuth2User user) {
        return new UserDTO(
                user.getName(),
                user.getAttribute("email"),
                user.getAttribute("nome-pessoa"),
                user.getAttribute("id-usuario"),
                0L, // user.getAttribute("id-discente"),
                user.getAttribute("id-institucional"),
                user.getAttribute("cpf-cnpj"),
                user.getAttribute("url-foto"),
                false);
    }
}
