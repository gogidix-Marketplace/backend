package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Preferences {
    @Builder.Default
    List<String> categories = List.of();
    @Builder.Default
    List<String> brands = List.of();
}
