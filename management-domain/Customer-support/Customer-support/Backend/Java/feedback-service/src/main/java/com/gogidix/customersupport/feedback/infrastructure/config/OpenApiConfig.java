package com.gogidix.customersupport.feedback.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI Configuration
 */
@Configuration
public class OpenApiConfig {

    @Value("${info.app.name:Feedback Service}")
    private String appName;

    @Value("${info.app.description:Customer Feedback Collection and Analysis Service}")
    private String appDescription;

    @Value("${info.app.version:1.0.0}")
    private String appVersion;

    @Bean
    public OpenAPI feedbackServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .description(appDescription)
                        .version(appVersion)
                        .contact(new Contact()
                                .name("Gogidix Support")
                                .email("support@gogidix.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("http://localhost:8111").description("Local Development Server"),
                        new Server().url("https://api.gogidix.com/feedback").description("Production Server")
                ));
    }
}
