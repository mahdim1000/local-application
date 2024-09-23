package org.radargps.localapplication.data_receiver.message.publisher;

import org.radargps.localapplication.common.outbox.DomainEvent;
import org.radargps.localapplication.common.outbox.EventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PalletEventPublisher implements EventPublisher {
    @Value("${direct.exchange}")
    private String exchangeName;

    @Value("${data.routing.key}")
    private String dataRoutingKey;

    private final RabbitTemplate template;

    public PalletEventPublisher(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public void publish(DomainEvent event) {
        template.convertAndSend(exchangeName, dataRoutingKey, event);
    }
}