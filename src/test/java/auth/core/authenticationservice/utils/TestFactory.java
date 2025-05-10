package auth.core.authenticationservice.utils;

import auth.core.authenticationservice.dto.request.RegistrationRequestDto;
import auth.core.authenticationservice.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class TestFactory {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static RegistrationRequestDto buildRegistrationRequestDto() {
        return new RegistrationRequestDto("FirstName", "LastName", "test@security.com", "password");
    }

    public static User buildUser(
            String email, String password, boolean isEmailVerified
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmailVerified(isEmailVerified);

        return user;
    }

}
