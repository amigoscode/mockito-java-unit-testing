package com.amigoscode;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record Order(
        UUID id,
        User user,
        BigDecimal amount,
        ZonedDateTime zonedDateTime
) {
}
