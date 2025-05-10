package auth.core.authenticationservice.repo;

import auth.core.authenticationservice.model.User;
import auth.core.authenticationservice.config.TestContainersConfig;
import auth.core.authenticationservice.utils.TestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserRepositoryTest extends TestContainersConfig {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userRepository.save(
                TestFactory.buildUser("inactive_user@security.com", "password", false));
        userRepository.save(
                TestFactory.buildUser("active_user@security.com", "password", true));
    }

    @Test
    void findByEmail() {
        // given

        // when
        Optional<User> userOp = userRepository.findByEmail("inactive_user@security.com");

        // then
        Assertions.assertNotNull(userOp);
        Assertions.assertTrue(userOp.isPresent());
    }

    @Test
    void findByEmailAndEmailVerifiedIsTrue() {
        // given

        // when
        Optional<User> inactiveUserExist = userRepository.findByEmailAndEmailVerifiedIsTrue("inactive_user@security.com");

        Optional<User> activeUserExist = userRepository.findByEmailAndEmailVerifiedIsTrue("active_user@security.com");

        //then
        Assertions.assertTrue(inactiveUserExist.isEmpty());
        Assertions.assertTrue(activeUserExist.isPresent());
    }

    @Test
    void existsByEmail() {
        // given

        // when
        boolean userExist = userRepository.existsByEmail("inactive_user@security.com");

        //then
        Assertions.assertTrue(userExist);
    }
}
