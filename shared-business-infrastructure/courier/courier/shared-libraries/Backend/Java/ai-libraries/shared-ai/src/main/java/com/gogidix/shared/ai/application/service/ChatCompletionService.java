package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.AiModelType;
import com.gogidix.shared.ai.domain.model.ChatMessage;
import com.gogidix.shared.ai.domain.model.ChatResponse;
import com.gogidix.shared.ai.domain.port.in.ChatCompletionUseCase;
import com.gogidix.shared.ai.domain.port.out.LlmProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatCompletionService implements ChatCompletionUseCase {

    private final List<LlmProviderPort> providers;

    @Override
    public ChatResponse complete(AiModelType model, List<ChatMessage> messages) {
        return complete(model, messages, 0.7, 4096);
    }

    @Override
    public ChatResponse complete(AiModelType model, List<ChatMessage> messages, double temperature, int maxTokens) {
        LlmProviderPort provider = resolveProvider(model);
        return provider.chat(model, messages, temperature, maxTokens);
    }

    @Override
    public ChatResponse completeWithSystemPrompt(AiModelType model, String systemPrompt, String userMessage) {
        List<ChatMessage> messages = List.of(
                ChatMessage.builder().role(ChatMessage.Role.SYSTEM).content(systemPrompt).build(),
                ChatMessage.builder().role(ChatMessage.Role.USER).content(userMessage).build()
        );
        return complete(model, messages);
    }

    private LlmProviderPort resolveProvider(AiModelType model) {
        return providers.stream()
                .filter(p -> p.supports(model))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No provider found for model: " + model));
    }
}
