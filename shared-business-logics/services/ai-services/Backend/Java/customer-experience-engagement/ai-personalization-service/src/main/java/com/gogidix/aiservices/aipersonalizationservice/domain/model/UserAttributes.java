package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Value
@Builder
public class UserAttributes {
    Demographics demographics;
    @Builder.Default
    List<String> interests = new ArrayList<>();
    Preferences preferences;

    public UserAttributes withInterests(List<String> interests) {
        List<String> uniqueInterests = interests.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        return new UserAttributes(demographics, uniqueInterests, preferences);
    }

    public boolean hasInterest(String interest) {
        return interests != null && interests.contains(interest);
    }
}
