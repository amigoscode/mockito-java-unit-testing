package com.amigoscode;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class OrderService {
    private final PaymentProcessor paymentProcessor;
    private final OrderRepository orderRepository;

    public OrderService(PaymentProcessor paymentProcessor,
                        OrderRepository orderRepository) {
        this.paymentProcessor = paymentProcessor;
        this.orderRepository = orderRepository;
    }

    public boolean processOrder(User user, BigDecimal amount) {
        boolean isCharged = paymentProcessor.charge(amount);
        if (!isCharged) {
            throw new IllegalStateException("Payment failed");
        }
        Order order = new Order(
             UUID.randomUUID(),
             user,
             amount,
             ZonedDateTime.now()
        );
        int saveResult = orderRepository.save(order);
        return saveResult == 1;
    }
}
