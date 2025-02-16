package auth.core.authenticationservice.controller;

import auth.core.authenticationservice.dto.request.AuthenticationRequestDto;
import auth.core.authenticationservice.dto.response.AuthenticationResponseDto;
import auth.core.authenticationservice.service.AuthenticationService;
import auth.core.authenticationservice.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static auth.core.authenticationservice.dto.AuthTokens.REFRESH_TOKEN_COOKIE_NAME;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sing-in")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody final AuthenticationRequestDto authenticationDto) {
        final var authTokens = authenticationService.authenticate(authenticationDto.email(), authenticationDto.password());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieUtil.addCookie(REFRESH_TOKEN_COOKIE_NAME,
                        authTokens.refreshToken(), authTokens.refreshTokenTtl()).toString())
                .body(new AuthenticationResponseDto(authTokens.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) final String refreshToken) {
        final var authTokens = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(new AuthenticationResponseDto(authTokens.accessToken()));
    }

    @PostMapping("sign-out")
    public ResponseEntity<Void> revokeToken(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) final String refreshToken) {
        authenticationService.revokeRefreshToken(refreshToken);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, CookieUtil.removeCookie(REFRESH_TOKEN_COOKIE_NAME).toString())
                .build();
    }
}
