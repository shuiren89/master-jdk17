package com.sms.ok.module.infra.framework.codegen.config;

import com.sms.ok.module.infra.enums.codegen.CodegenFrontTypeEnum;
import com.sms.ok.module.infra.enums.codegen.CodegenVOTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;

@ConfigurationProperties(prefix = "yudao.codegen")
@Validated
@Data
public class CodegenProperties {

    /**
     * 生成的 Java 代码的基础包
     */
    @NotNull(message = "Java 代码的基础包不能为空")
    private String basePackage;

    /**
     * 数据库名数组
     */
    @NotEmpty(message = "数据库不能为空")
    private Collection<String> dbSchemas;


    /**
     * 是否生成批量删除接口
     */
    @NotNull(message = "是否生成批量删除接口不能为空")
    private Boolean deleteBatchEnable;

    /**
     * 是否生成单元测试
     */
    @NotNull(message = "是否生成单元测试不能为空")
    private Boolean unitTestEnable;

}
