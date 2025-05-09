package auth.core.authenticationservice.repo;

import auth.core.authenticationservice.model.LoginAttempt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends CrudRepository<LoginAttempt, Long> {
}
