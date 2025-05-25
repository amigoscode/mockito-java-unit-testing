package com.amigoscode;

import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;

public class VerificationCodeService {
    private final Clock clock;
    private final Duration expiryDuration;

    public VerificationCodeService(Clock clock,
                                   Duration expiryDuration) {
        this.clock = clock;
        this.expiryDuration = expiryDuration;
    }

    public boolean isExpired(VerificationCode code) {
        ZonedDateTime now = ZonedDateTime.now(clock);
        Duration elapsed = Duration.between(code.createdAt(), now);
        return elapsed.compareTo(expiryDuration) > 0;
    }
}
