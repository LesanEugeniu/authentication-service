package auth.core.authenticationservice.service;

import auth.core.authenticationservice.exception.RestErrorResponseException;
import auth.core.authenticationservice.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static auth.core.authenticationservice.exception.ErrorType.EMAIL_VERIFICATION_REQUIRED;
import static auth.core.authenticationservice.exception.ProblemDetailBuilder.forStatusAndDetail;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(user -> {
            if (!user.isEmailVerified()) {
                throw new RestErrorResponseException(
                        forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Email verification required")
                                .withProperty("email", user.getEmail())
                                .withErrorType(EMAIL_VERIFICATION_REQUIRED)
                                .build()
                );
            }
            return org.springframework.security.core.userdetails.User.builder()
                    .username(email)
                    .password(user.getPassword())
                    .disabled(!user.isEmailVerified())
                    .build();
        }).orElseThrow(() -> new UsernameNotFoundException("User with username [%s] not found".formatted(email)));
    }

}
