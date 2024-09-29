package org.radargps.localapplication.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Value("${topic.exchange}")
    public String exchangeName;

    @Value("${product.scanner.queue.name}")
    public String productScannerQueueName;
    @Value("${product.scanner.routing.key}")
    public String productScannerRoutingKey;

    @Value("${pallet.scanner.queue.name}")
    public String palletScannerQueueName;
    @Value("${pallet.scanner.routing.key}")
    public String palletScannerRoutingKey;

    @Value("${unassign.pallet.queue.name}")
    public String unAssignPalletQueueName;
    @Value("${unassign.pallet.routing.key}")
    public String unAssignPalletRoutingKey;

    @Value("${unassign.product.queue.name}")
    public String unAssignProductQueueName;
    @Value("${unassign.product.routing.key}")
    public String unAssignProductRoutingKey;

    @Value("${pallet.product.queue.name}")
    public String palletProductQueueName;
    @Value("${pallet.product.routing.key}")
    public String palletProductRoutingKey;

    @Value("${product.product.queue.name}")
    public String productProductQueueName;
    @Value("${product.product.routing.key}")
    public String productProductRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }


    // --------------
    @Bean
    public Queue productScannerQueue() {
        return new Queue(productScannerQueueName, true);
    }

    @Bean
    public Binding productScannerDataBinding(Queue productScannerQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productScannerQueue).to(exchange).with(productScannerRoutingKey);
    }

    //-------------
    @Bean
    public Queue palletScannerQueue() {
        return new Queue(palletScannerQueueName, true);
    }

    @Bean
    public Binding palletScannerDataBinding(Queue palletScannerQueue, TopicExchange exchange) {
        return BindingBuilder.bind(palletScannerQueue).to(exchange).with(palletScannerRoutingKey);
    }

    // --------
    @Bean
    public Queue unAssignPalletQueue() {
        return new Queue(unAssignPalletQueueName, true);
    }

    @Bean
    public Binding unAssignPalletDataBinding(Queue unAssignPalletQueue, TopicExchange exchange) {
        return BindingBuilder.bind(unAssignPalletQueue).to(exchange).with(unAssignPalletRoutingKey);
    }

    //---------

    @Bean
    public Queue unAssignProductQueue() {
        return new Queue(unAssignProductQueueName, true);
    }
    @Bean
    public Binding unAssignProductDataBinding(Queue unAssignProductQueue, TopicExchange exchange) {
        return BindingBuilder.bind(unAssignProductQueue).to(exchange).with(unAssignProductRoutingKey);
    }
    //---------
    @Bean
    public Queue palletProductQueue() {
        return new Queue(palletProductQueueName, true);
    }
    @Bean
    public Binding palletProductDataBinding(Queue palletProductQueue, TopicExchange exchange) {
        return BindingBuilder.bind(palletProductQueue).to(exchange).with(palletProductRoutingKey);
    }
    //-----------
    @Bean
    public Queue productProductQueue() {
        return new Queue(productProductQueueName, true);
    }
    @Bean
    public Binding productProductDataBinding(Queue productProductQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productProductQueue).to(exchange).with(productProductRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
