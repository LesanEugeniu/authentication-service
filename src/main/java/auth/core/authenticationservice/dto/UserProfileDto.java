package auth.core.authenticationservice.dto;

public record UserProfileDto(
        String firstName,
        String lastName,
        String email,
        boolean emailVerified
) {
}
