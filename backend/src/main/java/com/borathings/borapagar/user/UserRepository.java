package com.borathings.borapagar.user;

import com.borathings.borapagar.core.SoftDeletableRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** UserRepository */
@Repository
public interface UserRepository extends SoftDeletableRepository<UserEntity> {
    Optional<UserEntity> findByIdUsuario(int idUsuario);

    Optional<UserEntity> findByLogin(String login);
}
