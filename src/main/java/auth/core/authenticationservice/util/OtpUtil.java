package auth.core.authenticationservice.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.stream.IntStream;

@UtilityClass
public class OtpUtil {

    private static final SecureRandom SECURE_RANDOM =new SecureRandom();

    public static String generateOtp(final Integer length) {
        StringBuilder otp = new StringBuilder();

        IntStream.range(0, length).mapToObj(i -> SECURE_RANDOM.nextInt(10)).forEach(otp::append);
        return otp.toString();
    }
}
