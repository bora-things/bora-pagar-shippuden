package com.borathings.borapagar.user.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

public record UserDTO(
        String login,
        String email,
        String personName,
        int userId,
        Long institutionalId,
        String cpf,
        String imageUrl,
        boolean deleted) {
    public static UserDTO fromSigaaUser(OAuth2User user) {
        return new UserDTO(
                user.getName(),
                user.getAttribute("email"),
                user.getAttribute("nome-pessoa"),
                user.getAttribute("id-usuario"),
                user.getAttribute("id-institucional"),
                parseCpf(user.getAttribute("cpf-cnpj")),
                user.getAttribute("url-foto"),
                false);
    }

    private static Long getLongAttribute(OAuth2User user, String key) {
        Object value = user.getAttribute(key);
        return (value instanceof Number) ? ((Number) value).longValue() : 0L;
    }

    private static int getIntAttribute(OAuth2User user, String key) {
        Object value = user.getAttribute(key);
        return (value instanceof Number) ? ((Number) value).intValue() : 0;
    }

    //    private static Long parseCpf(String cpf) {
    //        try {
    //            return cpf != null ? Long.parseLong(cpf.replaceAll("\\D", "")) : 0L;
    //        } catch (NumberFormatException e) {
    //            return 0L;
    //        }
    //    }

    private static String parseCpf(Long cpf) {
        return cpf != null ? String.format("%011d", cpf) : "";
    }
}
