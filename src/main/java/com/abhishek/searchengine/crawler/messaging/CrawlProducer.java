package com.abhishek.searchengine.crawler.messaging;

import com.abhishek.searchengine.config.RabbitMQConfig;
import com.abhishek.searchengine.crawler.dto.CrawlTask;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publish(CrawlTask task) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CRAWL_EXCHANGE,
                RabbitMQConfig.CRAWL_ROUTING_KEY,
                task
        );
    }
}