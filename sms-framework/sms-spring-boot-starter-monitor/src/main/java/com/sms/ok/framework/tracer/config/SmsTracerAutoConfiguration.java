package com.sms.ok.framework.tracer.config;

import com.sms.ok.framework.common.enums.WebFilterOrderEnum;
import com.sms.ok.framework.tracer.core.aop.BizTraceAspect;
import com.sms.ok.framework.tracer.core.filter.TraceFilter;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Tracer 配置类
 *
 */
@AutoConfiguration
@ConditionalOnClass(name = {
        "org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer",
        "io.opentracing.Tracer"
})
@EnableConfigurationProperties(TracerProperties.class)
@ConditionalOnProperty(prefix = "sms.tracer", value = "enable", matchIfMissing = true)
public class SmsTracerAutoConfiguration {

    @Bean
    public TracerProperties bizTracerProperties() {
        return new TracerProperties();
    }

    @Bean
    public BizTraceAspect bizTracingAop() {
        return new BizTraceAspect(tracer());
    }

    @Bean
    public Tracer tracer() {
        // 创建 SkywalkingTracer 对象
        SkywalkingTracer tracer = new SkywalkingTracer();
        // 设置为 GlobalTracer 的追踪器
        GlobalTracer.registerIfAbsent(tracer);
        return tracer;
    }

    /**
     * 创建 TraceFilter 过滤器，响应 header 设置 traceId
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilter() {
        FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceFilter());
        registrationBean.setOrder(WebFilterOrderEnum.TRACE_FILTER);
        return registrationBean;
    }

}
