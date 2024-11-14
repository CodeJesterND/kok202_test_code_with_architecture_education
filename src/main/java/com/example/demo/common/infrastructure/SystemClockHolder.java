package com.example.demo.common.infrastructure;

import com.example.demo.common.application.port.ClockHolder;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public Long millis() {
        return Clock.systemUTC().millis();
    }

}
