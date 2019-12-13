package org.bmsource.dwh.charts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@ComponentScan
@EnableCaching
public class ChartsConfiguration {

    @Bean
    public RedisCacheManager cacheManager(@Autowired RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.create(connectionFactory);
    }

    @Bean(name = "tenantKeyGenerator")
    public KeyGenerator tenantKeyGenerator() {
        return new TenantKeyGenerator();
    }
}
