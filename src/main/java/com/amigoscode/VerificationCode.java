package com.amigoscode;

import java.time.ZonedDateTime;

public record VerificationCode(
        String code,
        ZonedDateTime createdAt
) {
}
