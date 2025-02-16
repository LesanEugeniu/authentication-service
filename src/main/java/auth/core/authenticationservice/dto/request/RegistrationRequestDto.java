package auth.core.authenticationservice.dto.request;

import auth.core.authenticationservice.dto.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequestDto(
        @NotBlank(message = "FirstName is required")
        String firstName,

        @NotBlank(message = "LastName is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String email,

        @Password
        String password
) {
}
