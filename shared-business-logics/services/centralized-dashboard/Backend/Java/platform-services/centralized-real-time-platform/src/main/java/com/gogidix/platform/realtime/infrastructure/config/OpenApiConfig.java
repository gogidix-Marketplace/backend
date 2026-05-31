package com.gogidix.platform.realtime.infrastructure.config;

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
 * OpenAPI configuration.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8915}")
    private int serverPort;

    @Bean
    public OpenAPI realTimePlatformOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local server");

        return new OpenAPI()
                .info(new Info()
                        .title("Centralized Real-Time Platform API")
                        .version("1.0.0")
                        .description("API for real-time data streaming and WebSocket connectivity")
                        .contact(new Contact()
                                .name("Gogidix Team")
                                .email("platform@gogidix.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(localServer));
    }
}
