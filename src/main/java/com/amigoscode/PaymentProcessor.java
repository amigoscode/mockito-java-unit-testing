package com.amigoscode;

import java.math.BigDecimal;

public interface PaymentProcessor {
    boolean charge(BigDecimal amount);
}
