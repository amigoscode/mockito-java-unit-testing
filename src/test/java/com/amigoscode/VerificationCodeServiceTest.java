package com.amigoscode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceTest {

    @Mock private Clock clock;

    private final ZoneId zoneId = ZoneId.of("Europe/London");
    private final ZonedDateTime fixedZdt
            = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, zoneId);

    private final Duration expiryDuration =
            Duration.of(15, ChronoUnit.MINUTES);

    private VerificationCodeService underTest;

    @BeforeEach
    void setUp() {
        underTest = new VerificationCodeService(
                clock,
                expiryDuration
        );
        given(clock.getZone()).willReturn(zoneId);
        given(clock.instant()).willReturn(fixedZdt.toInstant());
    }

    @Test
    void shouldReturnFalseWhenCodeIsNotExpired() {
        // given
        // when
        // then
    }
}