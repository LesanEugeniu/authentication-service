package auth.core.authenticationservice.service;

import auth.core.authenticationservice.config.OtpConfig.OtpConfigProperties;
import auth.core.authenticationservice.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@RequiredArgsConstructor
public class OtpService {

    private final OtpConfigProperties configProperties;

    private final RedisTemplate<String, String> redisTemplate;

    private final PasswordEncoder passwordEncoder;

    public String generateAndStoreOtp(final UUID id) {
        final var otp = OtpUtil.generateOtp(configProperties.length());
        final var cacheKey = getCacheKey(id);

        redisTemplate.opsForValue().set(cacheKey, passwordEncoder.encode(otp), configProperties.ttl());

        return otp;
    }

    public boolean isOtpValid(UUID id, String otp) {
        final String cacheKey = getCacheKey(id);

        return passwordEncoder.matches(otp, redisTemplate.opsForValue().get(cacheKey));
    }

    public void deleteOtp(final UUID id) {
        final String cacheKey = getCacheKey(id);

        redisTemplate.delete(cacheKey);
    }

    private String getCacheKey(UUID id) {
        return configProperties.cachePrefix().formatted(id);
    }

}
