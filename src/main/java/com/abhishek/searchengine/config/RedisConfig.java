package com.abhishek.searchengine.config;

import com.abhishek.searchengine.search.dto.SearchResponse;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, List<SearchResponse>> searchRedisTemplate(
            RedisConnectionFactory factory,
            ObjectMapper mapper) {

        RedisTemplate<String, List<SearchResponse>> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);

        template.setKeySerializer(
                new StringRedisSerializer()
        );

        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(
                        List.class,
                        SearchResponse.class
                );

        template.setValueSerializer(
                new Jackson2JsonRedisSerializer<>(
                        mapper,
                        type
                )
        );

        template.afterPropertiesSet();

        return template;
    }
}