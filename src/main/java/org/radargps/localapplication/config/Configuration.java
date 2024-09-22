package org.radargps.localapplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.HashedWheelTimer;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public HashedWheelTimer timer() {
        return new HashedWheelTimer();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
