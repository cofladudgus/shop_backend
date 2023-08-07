package com.shopping.mall.api.configure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Shopping Mall System API",
        version = "v1"
    )
)
@Configuration
public class OpenApiConfigure {

}