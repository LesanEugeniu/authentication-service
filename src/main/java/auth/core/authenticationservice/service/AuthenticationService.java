package auth.core.authenticationservice.service;

import auth.core.authenticationservice.dto.AuthTokens;
import auth.core.authenticationservice.model.LoginAttempt;
import auth.core.authenticationservice.model.RefreshToken;
import auth.core.authenticationservice.model.User;
import auth.core.authenticationservice.repo.LoginAttemptRepository;
import auth.core.authenticationservice.repo.RefreshTokenRepository;
import auth.core.authenticationservice.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static java.time.Duration.between;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${jwt.refresh-token-ttl}")
    private final Duration refreshTokenTtl;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final LoginAttemptRepository loginAttemptRepository;

    @Transactional
    public AuthTokens authenticate(final String email, final String password) {
        final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(email, password);

        final var user = userRepository.findByEmailAndEmailVerifiedIsTrue(email);
        try {
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            user.ifPresent(value -> saveLoginAttempt(value, false));
            throw e;
        }

//        user can't be null because is authenticated with same flow
        return authenticate(user.get());
    }

    public AuthTokens authenticate(User user) {
        final var accessToken = jwtService.generateToken(user.getEmail());

        final var refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(refreshTokenTtl));
        refreshTokenRepository.save(refreshTokenEntity);
        saveLoginAttempt(user, true);

        return new AuthTokens(accessToken, refreshTokenEntity.getId().toString(), between(Instant.now(), refreshTokenEntity.getExpiresAt()));
    }

    public AuthTokens refreshToken(final String refreshToken) {
        final var refreshTokenEntity = refreshTokenRepository.findByIdAndExpiresAtAfter(validateRefreshTokenFormat(refreshToken), Instant.now())
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired refresh token"));

        final var newAccessToken = jwtService.generateToken(refreshTokenEntity.getUser().getEmail());

        return new AuthTokens(newAccessToken, refreshToken, between(Instant.now(), refreshTokenEntity.getExpiresAt()));
    }

    public void revokeRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(validateRefreshTokenFormat(refreshToken));
    }

    private UUID validateRefreshTokenFormat(final String refreshToken) {
        try {
            return UUID.fromString(refreshToken);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }
    }

    public void saveLoginAttempt(User user, boolean isAuthenticated) {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUser(user);
        loginAttempt.setSuccess(isAuthenticated);

        loginAttemptRepository.save(loginAttempt);
    }

}
