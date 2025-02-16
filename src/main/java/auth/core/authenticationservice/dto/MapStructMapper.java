package auth.core.authenticationservice.dto;

import auth.core.authenticationservice.dto.request.RegistrationRequestDto;
import auth.core.authenticationservice.dto.response.RegistrationResponseDto;
import auth.core.authenticationservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    UserProfileDto toDto(final User user);

    User toEntity(final RegistrationRequestDto registrationRequestDto);

    RegistrationResponseDto toResponseDto(final User user);

}
