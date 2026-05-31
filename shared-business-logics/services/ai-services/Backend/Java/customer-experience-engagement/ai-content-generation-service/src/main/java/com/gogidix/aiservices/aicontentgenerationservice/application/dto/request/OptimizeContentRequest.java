package com.gogidix.aiservices.aicontentgenerationservice.application.dto.request;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizeContentRequest {

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Content type is required")
    private ContentType contentType;

    private String optimizationGoal;
}
