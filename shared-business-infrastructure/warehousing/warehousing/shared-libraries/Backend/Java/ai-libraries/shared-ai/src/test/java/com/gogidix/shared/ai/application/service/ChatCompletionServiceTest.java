package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.*;
import com.gogidix.shared.ai.domain.port.out.LlmProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ChatCompletionServiceTest {

    private ChatCompletionService service;
    private LlmProviderPort mockProvider;

    @BeforeEach
    void setUp() {
        mockProvider = mock(LlmProviderPort.class);
        when(mockProvider.supports(any(AiModelType.class))).thenReturn(true);
        when(mockProvider.getProviderName()).thenReturn("test-provider");
        service = new ChatCompletionService(List.of(mockProvider));
    }

    @Test
    void complete_delegatesToProvider() {
        ChatResponse expected = ChatResponse.builder()
                .id("chat-1")
                .model(AiModelType.GPT_4)
                .content("Hello")
                .tokenUsage(TokenUsage.of(10, 5))
                .build();
        when(mockProvider.chat(any(), any(), anyDouble(), anyInt())).thenReturn(expected);

        List<ChatMessage> messages = List.of(
                ChatMessage.builder().role(ChatMessage.Role.USER).content("Hi").build()
        );
        ChatResponse result = service.complete(AiModelType.GPT_4, messages);

        assertEquals("chat-1", result.getId());
        assertEquals("Hello", result.getContent());
        verify(mockProvider).chat(eq(AiModelType.GPT_4), eq(messages), eq(0.7), eq(4096));
    }

    @Test
    void complete_withCustomParams() {
        ChatResponse expected = ChatResponse.builder().id("chat-2").content("Response").build();
        when(mockProvider.chat(any(), any(), anyDouble(), anyInt())).thenReturn(expected);

        List<ChatMessage> messages = List.of(
                ChatMessage.builder().role(ChatMessage.Role.USER).content("Test").build()
        );
        ChatResponse result = service.complete(AiModelType.GPT_4, messages, 0.5, 2048);

        assertNotNull(result);
        verify(mockProvider).chat(eq(AiModelType.GPT_4), eq(messages), eq(0.5), eq(2048));
    }

    @Test
    void completeWithSystemPrompt_sendsSystemAndUserMessages() {
        ChatResponse expected = ChatResponse.builder().id("chat-3").content("Result").build();
        when(mockProvider.chat(any(), any(), anyDouble(), anyInt())).thenReturn(expected);

        ChatResponse result = service.completeWithSystemPrompt(AiModelType.GPT_4, "You are helpful", "What is AI?");

        assertNotNull(result);
        verify(mockProvider).chat(eq(AiModelType.GPT_4), argThat(msgs -> msgs.size() == 2), anyDouble(), anyInt());
    }

    @Test
    void complete_throwsWhenNoProvider() {
        service = new ChatCompletionService(List.of());
        List<ChatMessage> messages = List.of(
                ChatMessage.builder().role(ChatMessage.Role.USER).content("Hi").build()
        );
        assertThrows(IllegalArgumentException.class, () -> service.complete(AiModelType.GPT_4, messages));
    }

    @Test
    void complete_throwsWhenProviderDoesNotSupportModel() {
        when(mockProvider.supports(AiModelType.CLAUDE_3_OPUS)).thenReturn(false);
        List<ChatMessage> messages = List.of(
                ChatMessage.builder().role(ChatMessage.Role.USER).content("Hi").build()
        );
        assertThrows(IllegalArgumentException.class, () -> service.complete(AiModelType.CLAUDE_3_OPUS, messages));
    }
}
