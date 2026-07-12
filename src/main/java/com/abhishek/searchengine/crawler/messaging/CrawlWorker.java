package com.abhishek.searchengine.crawler.messaging;

import com.abhishek.searchengine.config.RabbitMQConfig;
import com.abhishek.searchengine.crawler.dto.CrawlTask;
import com.abhishek.searchengine.crawler.service.CrawlService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.net.URI;

@Slf4j
@Service
@AllArgsConstructor
public class CrawlWorker {

    private final CrawlService crawlService;

    @RabbitListener(queues = RabbitMQConfig.CRAWL_QUEUE)
    public void process(CrawlTask task) {

        log.info("======================================");
        log.info("Received Crawl Task");
        log.info("URL   : {}", task.url());
        log.info("Depth : {}", task.depth());
        log.info("======================================");

        URI seedUri = URI.create(task.url());

        crawlService.crawl(new CrawlTask(task.url(), 0, seedUri.getHost()));
    }
}