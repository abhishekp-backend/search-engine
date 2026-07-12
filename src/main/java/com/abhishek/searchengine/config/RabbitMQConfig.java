package com.abhishek.searchengine.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CRAWL_QUEUE = "crawl.queue";
    public static final String CRAWL_EXCHANGE = "crawl.exchange";
    public static final String CRAWL_ROUTING_KEY = "crawl.routing.key";

    @Bean
    public Queue crawlQueue() {
        return new Queue(CRAWL_QUEUE, true);
    }

    @Bean
    public DirectExchange crawlExchange() {
        return new DirectExchange(CRAWL_EXCHANGE);
    }

    @Bean
    public Binding crawlBinding(Queue crawlQueue, DirectExchange crawlExchange) {
        return BindingBuilder
                .bind(crawlQueue)
                .to(crawlExchange)
                .with(CRAWL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}