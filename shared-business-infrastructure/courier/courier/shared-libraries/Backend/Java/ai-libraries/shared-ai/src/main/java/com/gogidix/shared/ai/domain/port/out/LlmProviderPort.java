package com.gogidix.shared.ai.domain.port.out;

import com.gogidix.shared.ai.domain.model.AiModelType;
import com.gogidix.shared.ai.domain.model.ChatMessage;
import com.gogidix.shared.ai.domain.model.ChatResponse;

import java.util.List;

public interface LlmProviderPort {

    ChatResponse chat(AiModelType model, List<ChatMessage> messages, double temperature, int maxTokens);

    boolean supports(AiModelType model);

    String getProviderName();
}
