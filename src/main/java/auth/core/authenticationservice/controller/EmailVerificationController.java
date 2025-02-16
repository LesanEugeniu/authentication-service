package auth.core.authenticationservice.controller;

import auth.core.authenticationservice.dto.request.EmailVerificationRequestDto;
import auth.core.authenticationservice.dto.response.AuthenticationResponseDto;
import auth.core.authenticationservice.service.AuthenticationService;
import auth.core.authenticationservice.service.EmailVerificationService;
import auth.core.authenticationservice.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static auth.core.authenticationservice.dto.AuthTokens.REFRESH_TOKEN_COOKIE_NAME;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    private final AuthenticationService authenticationService;

    @PostMapping("/request-verification-email")
    public ResponseEntity<Void> resendVerificationOtp(@RequestParam final String email) {
        emailVerificationService.resendEmailVerificationOtp(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthenticationResponseDto> verifyOtp(@Valid @RequestBody final EmailVerificationRequestDto emailVerificationDto) {
        final var verifiedUser = emailVerificationService.verifyEmailOtp(emailVerificationDto.email(), emailVerificationDto.otp());
        final var authTokens = authenticationService.authenticate(verifiedUser);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieUtil.addCookie(REFRESH_TOKEN_COOKIE_NAME, authTokens.refreshToken(), authTokens.refreshTokenTtl()).toString())
                .body(new AuthenticationResponseDto(authTokens.accessToken()));
    }

}
