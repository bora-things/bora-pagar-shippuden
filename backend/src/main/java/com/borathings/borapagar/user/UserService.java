package com.borathings.borapagar.user;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/** UserService */
@Service
public class UserService {
    @Autowired UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * upsert vem de update or insert, este método verifica se um OidcUser possui registro no banco
     * de dados, caso tenha, ele chama o método <code>updateExistingOidcUser</code>, senão, chama o
     * método <code>insertNewOidcUser</code>
     */
    public void upsertFromOidcUser(OidcUser oidcUser) {
        String oidcUserGoogleId = oidcUser.getSubject();
        Optional<UserEntity> user = userRepository.findByIdUsuario(oidcUserGoogleId);
        user.ifPresentOrElse(
                existingUser -> this.updateExistingOidcUser(existingUser, oidcUser),
                () -> insertNewOidcUser(oidcUser));
    }

    /**
     * Método invocado quando o OidcUser não possui registro equivalente no Banco de Dados, neste
     * caso é criado um registro novo no banco de dados utilizando as informações do perfil do
     * google
     */
    private void insertNewOidcUser(OidcUser oidcUser) {
        String oidcUserEmail = oidcUser.getEmail();
        String oidcUserSigaaID = oidcUser.getSubject();

        logger.info(
                "Novo usuário logado, criando conta para o email {} com SIGAA ID {}",
                oidcUserEmail,
                oidcUserSigaaID);
        UserEntity userEntity =
                UserEntity.builder()
                        .email(oidcUserEmail)
                        .name(oidcUser.getFullName())
                        .imageUrl(oidcUser.getPicture())
                        .build();
        userRepository.save(userEntity);
    }

    /** Atualiza as informações que podem ter mudado do OidcUser */
    private void updateExistingOidcUser(UserEntity existingUser, OidcUser oidcUser) {
        logger.info(
                "Usuário com SIGAA ID {} de email {} já existe no sistema, atualizando dados",
                existingUser.getId(),
                existingUser.getEmail());

        existingUser.setImageUrl(oidcUser.getPicture());
        userRepository.save(existingUser);
    }

    /**
     * Recupera um usuário do banco de dados pelo id da sua conta do SIGAA. Retorna <code>
     * EntityNotFoundException</code> caso o usuário não seja encontrado
     *
     * @param idUsuario - String - ID da conta do google
     * @return UserEntity - Usuário encontrado
     * @throws EntityNotFoundException - Se o usuário não for encontrado
     */
    public UserEntity findByIdUsuarioOrError(String idUsuario) {
        return userRepository
                .findByIdUsuario(idUsuario)
                .orElseThrow(
                        () -> {
                            return new EntityNotFoundException("Usuário não encontrado");
                        });
    }
}
