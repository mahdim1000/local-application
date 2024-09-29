package org.radargps.localapplication.scanner.device.message.publisher;

import org.radargps.localapplication.common.outbox.DomainEvent;
import org.radargps.localapplication.common.outbox.EventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductScannerEventPublisher implements EventPublisher {

    @Value("${topic.exchange}")
    private String exchangeName;

    @Value("${product.scanner.routing.key}")
    private String routingKey;

    private final RabbitTemplate template;
    public ProductScannerEventPublisher(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public void publish(DomainEvent event) {
        template.convertAndSend(exchangeName, routingKey, event);
    }
}
