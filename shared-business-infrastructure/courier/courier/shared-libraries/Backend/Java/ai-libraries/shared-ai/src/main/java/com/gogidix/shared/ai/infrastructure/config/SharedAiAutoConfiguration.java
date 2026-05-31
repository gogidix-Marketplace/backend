package com.gogidix.shared.ai.infrastructure.config;

import com.gogidix.shared.ai.application.config.AiClientConfig;
import com.gogidix.shared.ai.application.service.ChatCompletionService;
import com.gogidix.shared.ai.application.service.EmbeddingService;
import com.gogidix.shared.ai.application.service.PromptTemplateService;
import com.gogidix.shared.ai.application.service.TokenCountingService;
import com.gogidix.shared.ai.domain.port.in.ChatCompletionUseCase;
import com.gogidix.shared.ai.domain.port.in.EmbeddingUseCase;
import com.gogidix.shared.ai.domain.port.in.TokenCountingUseCase;
import com.gogidix.shared.ai.domain.port.out.EmbeddingProviderPort;
import com.gogidix.shared.ai.domain.port.out.LlmProviderPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = SharedAiAutoConfiguration.class)
public class SharedAiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ChatCompletionUseCase.class)
    public ChatCompletionUseCase chatCompletionUseCase(List<LlmProviderPort> providers) {
        return new ChatCompletionService(providers);
    }

    @Bean
    @ConditionalOnMissingBean(EmbeddingUseCase.class)
    public EmbeddingUseCase embeddingUseCase(List<EmbeddingProviderPort> providers) {
        return new EmbeddingService(providers);
    }

    @Bean
    @ConditionalOnMissingBean(TokenCountingUseCase.class)
    public TokenCountingUseCase tokenCountingUseCase() {
        return new TokenCountingService();
    }

    @Bean
    @ConditionalOnMissingBean(PromptTemplateService.class)
    public PromptTemplateService promptTemplateService() {
        return new PromptTemplateService();
    }
}
