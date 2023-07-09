package com.yukikyu.kook.boot.app.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    /*@Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("127.0.0.1");
        config.setPort(6379);
        // 如果需要密码，可以在这里设置
        // config.setPassword(RedisPassword.of("yourPassword"));

        return new LettuceConnectionFactory(config);
    }*/

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 如果需要使用JSON序列化对象，请取消下面两行的注释
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setEnableDefaultSerializer(true);
        return template;
    }
}
