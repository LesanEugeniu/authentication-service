package auth.core.authenticationservice.repo;

import auth.core.authenticationservice.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.emailVerified = true")
    Optional<User> findByEmailAndEmailVerifiedIsTrue(@Param("email") String email);

    boolean existsByEmail(String email);

}
