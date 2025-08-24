package com.sms.ok.framework.idempotent.config;

import com.sms.ok.framework.idempotent.core.aop.IdempotentAspect;
import com.sms.ok.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.sms.ok.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.sms.ok.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import com.sms.ok.framework.idempotent.core.keyresolver.impl.UserIdempotentKeyResolver;
import com.sms.ok.framework.idempotent.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import com.sms.ok.framework.redis.config.SmsRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = SmsRedisAutoConfiguration.class)
public class SmsIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public UserIdempotentKeyResolver userIdempotentKeyResolver() {
        return new UserIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
