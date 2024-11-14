package com.example.demo.small.mock;

import com.example.demo.common.application.port.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final Long millis;

    @Override
    public Long millis() {
        return millis;
    }

}
