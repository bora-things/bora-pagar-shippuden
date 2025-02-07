package com.borathings.borapagar.user.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

public record UserDTO(
        String login,
        String email,
        String name,
        int idUsuario,
        Long idDiscente,
        Long idInstitucional,
        Long idUnidade,
        Long cpf,
        String imageUrl,
        boolean ativo,
        String nomeCurso,
        String siglaNivel,
        int anoIngresso,
        int periodoIngresso,
        int idFormaIngresso,
        String descricaoFormaIngresso,
        int idGestoraAcademica,
        int idTipoParticipante,
        int idInstituicaoEnsino,
        String instituicaoEnsino,
        int idPolo,
        String polo,
        boolean deleted) {

    public static UserDTO fromSigaaUser(OAuth2User user) {
        return new UserDTO(
                user.getAttribute("login") != null ? user.getAttribute("login") : "",
                user.getAttribute("email"),
                user.getAttribute("nome-pessoa"),
                user.getAttribute("id-usuario") != null ? user.getAttribute("id-usuario") : 0,
                getLongAttribute(user, "id-discente"),
                getLongAttribute(user, "id-institucional"),
                getLongAttribute(user, "id-unidade"),
                parseCpf(user.getAttribute("cpf-cnpj")),
                user.getAttribute("url-foto"),
                user.getAttribute("ativo") != null ? Boolean.TRUE.equals(user.getAttribute("ativo")) : false,
                user.getAttribute("nome-curso"),
                user.getAttribute("sigla-nivel"),
                getIntAttribute(user, "ano-ingresso"),
                getIntAttribute(user, "periodo-ingresso"),
                getIntAttribute(user, "id-forma-ingresso"),
                user.getAttribute("descricao-forma-ingresso"),
                getIntAttribute(user, "id-gestora-academica"),
                getIntAttribute(user, "id-tipo-participante"),
                getIntAttribute(user, "id-instituicao-ensino"),
                user.getAttribute("instituicao-ensino"),
                getIntAttribute(user, "id-polo"),
                user.getAttribute("polo"),
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

    private static Long parseCpf(String cpf) {
        try {
            return cpf != null ? Long.parseLong(cpf.replaceAll("\\D", "")) : 0L;
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
