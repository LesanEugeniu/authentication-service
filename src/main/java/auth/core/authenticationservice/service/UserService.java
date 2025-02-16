package auth.core.authenticationservice.service;


import auth.core.authenticationservice.exception.RestErrorResponseException;
import auth.core.authenticationservice.model.User;
import auth.core.authenticationservice.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static auth.core.authenticationservice.exception.ErrorType.ACCOUNT_UNAVAILABLE;
import static auth.core.authenticationservice.exception.ErrorType.RESOURCE_ALREADY_EXISTS;
import static auth.core.authenticationservice.exception.ProblemDetailBuilder.forStatusAndDetail;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.GONE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(final User user) {
        final var errors = new HashMap<String, List<String>>();

        if (userRepository.existsByEmail(user.getEmail())) {
            errors.put("email", List.of("Email is already taken"));
        }

        if (!errors.isEmpty()) {
            throw new RestErrorResponseException(forStatusAndDetail(CONFLICT, "Request validation failed")
                    .withProperty("errors", errors)
                    .withErrorType(RESOURCE_ALREADY_EXISTS)
                    .build()
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new RestErrorResponseException(forStatusAndDetail(GONE, "The user account has been deleted or inactivated")
                        .withErrorType(ACCOUNT_UNAVAILABLE)
                        .build()
                )
        );
    }

}
