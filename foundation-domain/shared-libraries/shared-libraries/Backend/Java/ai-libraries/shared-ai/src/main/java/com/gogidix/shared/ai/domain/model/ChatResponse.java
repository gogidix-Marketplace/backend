package com.gogidix.shared.ai.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class ChatResponse {

    String id;
    AiModelType model;
    String content;
    @Builder.Default
    Map<String, Object> metadata = Map.of();
    TokenUsage tokenUsage;
    @Builder.Default
    List<String> stopReasons = List.of();
}
