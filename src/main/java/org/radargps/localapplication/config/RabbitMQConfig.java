package org.radargps.localapplication.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_STAGE_QUEUE = "product-stage-queue";
    public static final String PALLET_STAGE_QUEUE = "pallet-stage-queue";
    public static final String ASSIGN_PALLET_PRODUCT_QUEUE = "assign-pallet-product-queue";
    public static final String UN_ASSIGN_PRODUCT_PALLET_QUEUE = "un-assign-product-pallet-queue";
    public static final String UN_ASSIGN_PALLET_PRODUCTS_QUEUE = "un-assign-pallet-products-queue";
    public static final String PRODUCT_LINK_QUEUE = "product-link-queue";


    @Value("${product.stage.routing.key}")
    private String productStageRoutingKey;

    @Value("${direct.exchange}")
    public String exchangeName;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue productStageQueue() {
        return new Queue(PRODUCT_STAGE_QUEUE, true);
    }

    @Bean
    public Queue palletStageQueue() {
        return new Queue(PALLET_STAGE_QUEUE, true);
    }

    @Bean
    public Queue assignPalletProductQueue() {
        return new Queue(ASSIGN_PALLET_PRODUCT_QUEUE, true);
    }

    @Bean
    public Queue unAssignProductPalletQueue() {
        return new Queue(UN_ASSIGN_PRODUCT_PALLET_QUEUE, true);
    }

    @Bean
    public Queue unAssignPalletProductsQueue() {
        return new Queue(UN_ASSIGN_PALLET_PRODUCTS_QUEUE, true);
    }

    @Bean
    public Queue productLinkQueue() {
        return new Queue(PRODUCT_LINK_QUEUE, true);
    }



    @Bean
    public Binding productStageBinding(Queue productStageQueue, DirectExchange exchange) {
        return BindingBuilder.bind(productStageQueue).to(exchange).with(productStageRoutingKey);
    }

    @Bean
    public Binding palletStageBinding(Queue palletStageQueue, DirectExchange exchange) {
        return BindingBuilder.bind(palletStageQueue).to(exchange).with("pallet.stage");
    }

    @Bean
    public Binding assignPalletProductBinding(Queue assignPalletProductQueue, DirectExchange exchange) {
        return BindingBuilder.bind(assignPalletProductQueue).to(exchange).with("assign.pallet.product");
    }

    @Bean
    public Binding unAssignProductPalletBinding(Queue assignPalletProductQueue, DirectExchange exchange) {
        return BindingBuilder.bind(assignPalletProductQueue).to(exchange).with("un-assign.pallet.product");
    }

    @Bean
    public Binding productLinkBinding(Queue productLinkQueue, DirectExchange exchange) {
        return BindingBuilder.bind(productLinkQueue).to(exchange).with("product.link");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
