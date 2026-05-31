package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.AiModelType;
import com.gogidix.shared.ai.domain.port.in.TokenCountingUseCase;
import org.springframework.stereotype.Service;

@Service
public class TokenCountingService implements TokenCountingUseCase {

    private static final double AVG_CHARS_PER_TOKEN = 4.0;

    @Override
    public int countTokens(String text, AiModelType model) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil(text.length() / AVG_CHARS_PER_TOKEN);
    }

    @Override
    public int countTokens(String text) {
        return countTokens(text, AiModelType.GPT_4);
    }

    @Override
    public boolean exceedsLimit(String text, AiModelType model, int maxTokens) {
        return countTokens(text, model) > maxTokens;
    }
}
