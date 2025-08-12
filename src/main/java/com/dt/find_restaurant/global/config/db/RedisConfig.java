package com.dt.find_restaurant.global.config.db;//package com.jangpyeong.fortuneteller.global.config.db;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@EnableRedisRepositories
//public class RedisConfig {
//
//    @Bean
//    public RedisTemplate<String, JwtAuthRedis> jwtRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, JwtAuthRedis> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
/// / /    @Bean /    public RedisTemplate<String, ResultCacheDto> resultCacheDtoRedisTemplate(RedisConnectionFactory
/// factory) { /        RedisTemplate<String, ResultCacheDto> template = new RedisTemplate<>(); /
/// template.setConnectionFactory(factory); /        template.setKeySerializer(new StringRedisSerializer()); /
/// template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); /        return template; /    }
//
//}