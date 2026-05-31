package com.gogidix.shared.ai.domain.port.in;

import com.gogidix.shared.ai.domain.model.AiModelType;
import com.gogidix.shared.ai.domain.model.ChatMessage;
import com.gogidix.shared.ai.domain.model.ChatResponse;

import java.util.List;

public interface ChatCompletionUseCase {

    ChatResponse complete(AiModelType model, List<ChatMessage> messages);

    ChatResponse complete(AiModelType model, List<ChatMessage> messages, double temperature, int maxTokens);

    ChatResponse completeWithSystemPrompt(AiModelType model, String systemPrompt, String userMessage);
}
