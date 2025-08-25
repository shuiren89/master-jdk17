package com.sms.ok.framework.tracer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BizTracer配置类
 *
 */
@ConfigurationProperties("yudao.tracer")
@Data
public class TracerProperties {
}
