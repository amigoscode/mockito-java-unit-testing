package com.amigoscode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PaymentProcessor paymentProcessor;

    @Mock
    private OrderRepository orderRepository;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @InjectMocks
    private OrderService underTest;

    @BeforeEach
    void setUp() {
//        underTest = new OrderService(paymentProcessor, orderRepository);
    }

    @Test
    void canChargeWithArgCaptor() {
        // given
        BigDecimal amount = BigDecimal.TEN;
        User user = new User(1, "James");
        when(paymentProcessor.charge(amount)).thenReturn(true);
        when(orderRepository.save(any())).thenReturn(1);

        // when
        boolean actual = underTest.processOrder(user, amount);
        // then

        InOrder inOrder = inOrder(orderRepository, paymentProcessor);

        inOrder.verify(orderRepository).save(orderArgumentCaptor.capture());
        inOrder.verify(paymentProcessor).charge(amount);

//        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

        Order orderArgumentCaptorValue = orderArgumentCaptor.getValue();
        assertThat(orderArgumentCaptorValue.amount()).isEqualTo(amount);
        assertThat(orderArgumentCaptorValue.user()).isEqualTo(user);
        assertThat(orderArgumentCaptorValue.id()).isNotNull();
        assertThat(orderArgumentCaptorValue.zonedDateTime())
                .isBefore(ZonedDateTime.now())
                .isNotNull();

        assertThat(actual).isTrue();
    }

    @Test
    void canChargeWithAssertArg() {
        // given
        BigDecimal amount = BigDecimal.TEN;
        User user = new User(1, "James");
        when(paymentProcessor.charge(amount)).thenReturn(true);
        when(orderRepository.save(any())).thenReturn(1);

        // when
        boolean actual = underTest.processOrder(user, amount);
        // then
        verify(paymentProcessor).charge(amount);

        verify(orderRepository).save(assertArg(order -> {
            assertThat(order.amount()).isEqualTo(amount);
            assertThat(order.user()).isEqualTo(user);
            assertThat(order.id()).isNotNull();
            assertThat(order.zonedDateTime())
                    .isBefore(ZonedDateTime.now())
                    .isNotNull();
        }));
        assertThat(actual).isTrue();
    }

    @Test
    void shouldThrowWhenChargeFails() {
        // given
        BigDecimal amount = BigDecimal.TEN;
        when(paymentProcessor.charge(amount)).thenReturn(false);
        // when
        assertThatThrownBy(() -> {
            underTest.processOrder(null, amount);
        })
                .hasMessageContaining("Payment failed")
                .isInstanceOf(IllegalStateException.class);
        // then
        verify(paymentProcessor).charge(amount);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowWhenChargeFailsWithMockitoBDD() {
        // given
        BigDecimal amount = BigDecimal.TEN;
        given(paymentProcessor.charge(amount)).willReturn(false);
        // when
        assertThatThrownBy(() -> {
            underTest.processOrder(null, amount);
        })
                .hasMessageContaining("Payment failed")
                .isInstanceOf(IllegalStateException.class);
        // then
        then(paymentProcessor).should().charge(amount);
        then(orderRepository).shouldHaveNoInteractions();
    }
}