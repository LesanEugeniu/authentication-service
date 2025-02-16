package auth.core.authenticationservice.controller;

import auth.core.authenticationservice.dto.MapStructMapper;
import auth.core.authenticationservice.dto.request.RegistrationRequestDto;
import auth.core.authenticationservice.dto.response.RegistrationResponseDto;
import auth.core.authenticationservice.service.EmailVerificationService;
import auth.core.authenticationservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    private final EmailVerificationService emailVerificationService;

    private final MapStructMapper mapper;

    @PostMapping("/sign-up")
    public ResponseEntity<RegistrationResponseDto> registerUser(
            @Valid @RequestBody final RegistrationRequestDto registrationDto) {
        final var registeredUser = userService.registerUser(mapper.toEntity(registrationDto));

        emailVerificationService.sendEmailVerificationOtp(registeredUser.getId(), registeredUser.getEmail());

        return ResponseEntity.ok(mapper.toResponseDto(registeredUser));
    }
}
