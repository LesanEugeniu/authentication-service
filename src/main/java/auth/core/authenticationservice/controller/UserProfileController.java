package auth.core.authenticationservice.controller;

import auth.core.authenticationservice.dto.MapStructMapper;
import auth.core.authenticationservice.dto.UserProfileDto;
import auth.core.authenticationservice.model.User;
import auth.core.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    private final MapStructMapper mapper;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(final Authentication authentication) {
        final User user = userService.getUserByEmail(authentication.getName());

        return ResponseEntity.ok(mapper.toDto(user));
    }
}
