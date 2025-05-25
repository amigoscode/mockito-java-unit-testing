package com.amigoscode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class AppTest {

    private final List<String> myMock = mock();
    private final List<String> names = new ArrayList<>();

    @Test
    void myFirstTestWithMock() {
        myMock.add("hello");
        when(myMock.get(0)).thenReturn("hello");

        verify(myMock).add("hello");

        String actual = myMock.get(0);
        assertThat(actual).isEqualTo("hello");
    }

    @Test
    void myFirstTestWithoutMock() {
        names.add("hello");
        assertThat(names).hasSize(1);
    }

    @Test
    void testAnyMatcher() {
        Map<String, String> mockMap = mock();
        when(mockMap.get(anyString())).thenReturn("hello");
        assertThat(mockMap.get("1")).isEqualTo("hello");
        assertThat(mockMap.get("2")).isEqualTo("hello");
        verify(mockMap, times(2)).get(anyString());
    }

    @Test
    void testEqMatcher() {
        Map<String, String> mockMap = mock();
        when(mockMap.put((anyString()), eq("1"))).thenReturn("hello");

        String actual = mockMap.put("hello", "1");

        assertThat(actual).isEqualTo("hello");
        verify(mockMap).put(eq("hello"), eq("1"));
    }

    @Test
    void shouldVerifyNoInteractions() {
        // given
        List<String> listMock = mock();
        // when
        // then
        verifyNoInteractions(listMock);
    }

    @Test
    void shouldVerifyNoMoreInteractions() {
        // given
        List<String> listMock = mock();
        // when
        listMock.clear();
        listMock.add("hello");
        // then
        verify(listMock).clear();
        verify(listMock).add("hello");
        verifyNoMoreInteractions(listMock);
    }

    @Test
    void shouldVerifyInteractionMode() {
        // given
        List<String> listMock = mock();
        // when
        listMock.clear();
        listMock.clear();
        // then
        verify(listMock, times(2)).clear();
        verifyNoMoreInteractions(listMock);
    }

    @Test
    void mockitoDbb() {
        // given
        List<String> mockList = mock();
        // when(mockList.get(0)).thenReturn("hello");
        given(mockList.get(0)).willReturn("hello");
        // when
        String actual = mockList.get(0);
        // then
        // verify(mockList).get(0);
        then(mockList).should().get(0);
        assertThat(actual).isEqualTo("hello");
    }

    @Test
    void chainedStubbing() {
        // given
        List<String> mockList = mock();
        // when
        given(mockList.size()).willReturn(1, 2, 3, 4);
//        when(mockList.size()).thenReturn(1, 2, 3, 4);
        // then
        assertThat(mockList.size()).isEqualTo(1);
        assertThat(mockList.size()).isEqualTo(2);
        assertThat(mockList.size()).isEqualTo(3);
        assertThat(mockList.size()).isEqualTo(4); // moving forward will always be 4
        assertThat(mockList.size()).isEqualTo(4);
    }

    @Test
    void shouldReturnCustomAnswer() {
        // given
        List<String> mockList = mock();
        // when
        given(mockList.get(anyInt())).will(i -> {
            int index = i.getArgument(0);
            return "Amigos: " + index;
        });
        // then
        assertThat(mockList.get(0)).isEqualTo("Amigos: 0");
        assertThat(mockList.get(1)).isEqualTo("Amigos: 1");
    }

    @Test
    void async() {
        // given
        Runnable mockRunnable = mock();
        // when
        Executors
                .newSingleThreadScheduledExecutor()
                .schedule(mockRunnable, 200, TimeUnit.MILLISECONDS);
        // then
        then(mockRunnable).should(timeout(500).times(1)).run();
    }

    @Test
    void canAdvanceClock() {
        // given
        Clock clock = mock();
        ZoneId zoneId = ZoneId.of("Europe/London");

        ZonedDateTime fixedZdt = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, zoneId);

        given(clock.getZone()).willReturn(zoneId);
        given(clock.instant()).willReturn(fixedZdt.toInstant());

        ZonedDateTime now = ZonedDateTime.now(clock);
        System.out.println(now);

        // advance the clock
        given(clock.instant()).willReturn(now.plusMinutes(15).toInstant());

        // call current time
        System.out.println(ZonedDateTime.now(clock));

        // advance the clock
        given(clock.instant()).willReturn(now.plusMonths(15).toInstant());

        // call current time
        System.out.println(ZonedDateTime.now(clock));
    }
}
