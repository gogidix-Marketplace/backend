package com.gogidix.shared.ai.domain.port.in;

import com.gogidix.shared.ai.domain.model.AiModelType;

public interface TokenCountingUseCase {

    int countTokens(String text, AiModelType model);

    int countTokens(String text);

    boolean exceedsLimit(String text, AiModelType model, int maxTokens);
}
