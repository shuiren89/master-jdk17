package com.sms.ok.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.TimeZone;

/**
 * 项目的启动类
 * @author 芋道源码
 */
@SpringBootApplication(scanBasePackages = {"com.sms.ok.server", "com.sms.ok.module"})
public class SmsServerApplication {

    public static void main(String[] args) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);

        ConfigurableApplicationContext ctx = SpringApplication.run(SmsServerApplication.class, args);
//        new SpringApplicationBuilder(YudaoServerApplication.class)
//                .applicationStartup(new BufferingApplicationStartup(20480))
//                .run(args);

    }

}
