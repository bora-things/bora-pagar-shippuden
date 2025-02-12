package com.borathings.borapagar.user;

import com.borathings.borapagar.user.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/** UserService */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * upsert vem de update or insert, este método verifica se um OidcUser possui registro no banco de dados, caso
     * tenha, ele chama o método <code>updateExistingOidcUser</code>, senão, chama o método <code>insertNewOidcUser
     * </code>
     */
    public void upsert(OAuth2User user) {
        logger.info("Recebido login de usuário {}", user.getName());
        String oidcUserGoogleId = user.getName();
        Optional<UserEntity> maybeUser = userRepository.findByUserId(user.getAttribute("id-usuario"));
        maybeUser.ifPresentOrElse(
                existingUser -> this.updateExistingOidcUser(existingUser, user), () -> insertNewUser(user));
    }

    /**
     * Método invocado quando o usuário não possui registro equivalente no Banco de Dados, neste caso é criado um
     * registro novo no banco de dados utilizando as informações do perfil do google
     */
    private void insertNewUser(OAuth2User user) {
        UserDTO u = UserDTO.fromSigaaUser(user);

        logger.info("Novo usuário logado, criando conta para o email {} com SIGAA ID {}", u.email(), user.getName());

        userRepository.save(userMapper.toEntity(u));
    }

    /** Atualiza as informações que podem ter mudado do OidcUser */
    private void updateExistingOidcUser(UserEntity existingUser, OAuth2User oidcUser) {
        logger.info(
                "Usuário com SIGAA ID {} de email {} já existe no sistema, atualizando dados",
                existingUser.getId(),
                existingUser.getEmail());

        existingUser.setImageUrl(oidcUser.getAttribute("imagem_url"));
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
    public UserEntity findByIdUserOrError(int idUsuario) {
        return userRepository.findByUserId(idUsuario).orElseThrow(() -> {
            return new EntityNotFoundException("Usuário não encontrado");
        });
    }

    public UserEntity findByLoginOrError(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> {
            return new EntityNotFoundException("Usuário não encontrado");
        });
    }
}
