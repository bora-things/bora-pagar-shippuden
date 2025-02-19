package com.borathings.borapagar.user;

import com.borathings.borapagar.core.SoftDeletableRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** UserRepository */
@Repository
public interface UserRepository extends SoftDeletableRepository<UserEntity> {
    Optional<UserEntity> findByUserId(int idUser);

    Optional<UserEntity> findByLogin(String login);
}
