package com.gogidix.shared.ai.infrastructure.config;

import com.gogidix.shared.ai.application.service.ChatCompletionService;
import com.gogidix.shared.ai.application.service.EmbeddingService;
import com.gogidix.shared.ai.application.service.PromptTemplateService;
import com.gogidix.shared.ai.application.service.TokenCountingService;
import com.gogidix.shared.ai.domain.port.in.ChatCompletionUseCase;
import com.gogidix.shared.ai.domain.port.in.EmbeddingUseCase;
import com.gogidix.shared.ai.domain.port.in.TokenCountingUseCase;
import com.gogidix.shared.ai.domain.port.out.EmbeddingProviderPort;
import com.gogidix.shared.ai.domain.port.out.LlmProviderPort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SharedAiAutoConfigurationTest {

    private final SharedAiAutoConfiguration config = new SharedAiAutoConfiguration();

    @Test
    void chatCompletionUseCase_createsService() {
        LlmProviderPort mockProvider = mock(LlmProviderPort.class);
        ChatCompletionUseCase useCase = config.chatCompletionUseCase(List.of(mockProvider));
        assertInstanceOf(ChatCompletionService.class, useCase);
    }

    @Test
    void embeddingUseCase_createsService() {
        EmbeddingProviderPort mockProvider = mock(EmbeddingProviderPort.class);
        EmbeddingUseCase useCase = config.embeddingUseCase(List.of(mockProvider));
        assertInstanceOf(EmbeddingService.class, useCase);
    }

    @Test
    void tokenCountingUseCase_createsService() {
        TokenCountingUseCase useCase = config.tokenCountingUseCase();
        assertInstanceOf(TokenCountingService.class, useCase);
    }

    @Test
    void promptTemplateService_createsService() {
        PromptTemplateService service = config.promptTemplateService();
        assertInstanceOf(PromptTemplateService.class, service);
    }
}
