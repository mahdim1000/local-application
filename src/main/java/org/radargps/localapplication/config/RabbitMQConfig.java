package org.radargps.localapplication.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DATA_QUEUE = "data-queue";
    public static final String DATA_CAPTURE_DEVICE_EVENT_QUEUE = "data-capture-device-event-queue";

    @Value("${data.routing.key}")
    private String dataRoutingKey;

    @Value("${data.capture.device.routing.key}")
    private String deviceRoutingKey;

    @Value("${direct.exchange}")
    public String exchangeName;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue dataQueue() {
        return new Queue(DATA_QUEUE, true);
    }

    @Bean
    public Queue dataCaptureDeviceEventQueue() {
        return new Queue(DATA_CAPTURE_DEVICE_EVENT_QUEUE, true);
    }

    @Bean
    public Binding dataBinding(Queue dataQueue, DirectExchange exchange) {
        return BindingBuilder.bind(dataQueue).to(exchange).with(dataRoutingKey);
    }

    @Bean
    public Binding deviceBinding(Queue dataCaptureDeviceEventQueue, DirectExchange exchange) {
        return BindingBuilder.bind(dataCaptureDeviceEventQueue).to(exchange).with(deviceRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
