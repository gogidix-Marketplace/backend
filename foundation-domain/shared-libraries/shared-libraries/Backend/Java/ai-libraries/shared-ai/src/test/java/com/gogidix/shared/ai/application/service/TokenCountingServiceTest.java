package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.AiModelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenCountingServiceTest {

    private TokenCountingService service;

    @BeforeEach
    void setUp() {
        service = new TokenCountingService();
    }

    @Test
    void countTokens_returnsEstimate() {
        int tokens = service.countTokens("Hello world");
        assertTrue(tokens > 0);
        assertEquals(3, tokens);
    }

    @Test
    void countTokens_nullReturnsZero() {
        assertEquals(0, service.countTokens(null));
    }

    @Test
    void countTokens_emptyReturnsZero() {
        assertEquals(0, service.countTokens(""));
    }

    @Test
    void countTokens_withModel_returnsSameEstimate() {
        String text = "This is a test sentence for token counting";
        int tokensDefault = service.countTokens(text);
        int tokensGpt4 = service.countTokens(text, AiModelType.GPT_4);
        assertEquals(tokensDefault, tokensGpt4);
    }

    @Test
    void exceedsLimit_trueWhenOver() {
        String text = "a".repeat(1000);
        assertTrue(service.exceedsLimit(text, AiModelType.GPT_4, 10));
    }

    @Test
    void exceedsLimit_falseWhenUnder() {
        String text = "short";
        assertFalse(service.exceedsLimit(text, AiModelType.GPT_4, 100));
    }
}
