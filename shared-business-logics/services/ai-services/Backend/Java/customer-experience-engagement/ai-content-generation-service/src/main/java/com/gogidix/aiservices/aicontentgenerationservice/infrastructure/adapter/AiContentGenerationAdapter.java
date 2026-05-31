package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.adapter;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GeneratedContent;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentGenerationPort;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.config.AiServiceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiContentGenerationAdapter implements ContentGenerationPort {

    private final RestTemplate restTemplate;
    private final AiServiceProperties properties;

    @Override
    public GeneratedContent generateContent(String prompt, ContentType type, ContentTone tone, String language) {
        try {
            String url = properties.getBaseUrl() + properties.getGenerationEndpoint();

            Map<String, Object> request = buildGenerationRequest(prompt, type, tone, language);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || !response.containsKey("content")) {
                return generateFallbackContent(prompt, type, tone);
            }

            String generatedContent = (String) response.get("content");

            return GeneratedContent.builder()
                    .contentId(UUID.randomUUID().toString())
                    .content(generatedContent)
                    .prompt(prompt)
                    .contentType(type)
                    .tone(tone)
                    .language(language)
                    .build();

        } catch (Exception e) {
            log.error("Failed to generate content via AI service: {}", e.getMessage());
            return generateFallbackContent(prompt, type, tone);
        }
    }

    @Override
    public List<GeneratedContent> generateVariations(String prompt, ContentType type, ContentTone tone,
                                                     String language, int count) {
        try {
            String url = properties.getBaseUrl() + properties.getVariationsEndpoint();

            Map<String, Object> request = Map.of(
                    "prompt", prompt,
                    "contentType", type.toString(),
                    "tone", tone != null ? tone.toString() : "neutral",
                    "language", language != null ? language : "en",
                    "count", count
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response != null && response.containsKey("variations")) {
                @SuppressWarnings("unchecked")
                List<String> variations = (List<String>) response.get("variations");

                return variations.stream()
                        .map(content -> GeneratedContent.builder()
                                .contentId(UUID.randomUUID().toString())
                                .content(content)
                                .prompt(prompt)
                                .contentType(type)
                                .tone(tone)
                                .language(language)
                                .build())
                        .collect(Collectors.toList());
            }

        } catch (Exception e) {
            log.error("Failed to generate variations via AI service: {}", e.getMessage());
        }

        return generateFallbackVariations(prompt, type, tone, language, count);
    }

    @Override
    public String optimizeContent(String content, ContentType type, String optimizationGoal) {
        try {
            String url = properties.getBaseUrl() + properties.getOptimizationEndpoint();

            Map<String, Object> request = Map.of(
                    "content", content,
                    "contentType", type.toString(),
                    "goal", optimizationGoal != null ? optimizationGoal : "general"
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response != null && response.containsKey("optimizedContent")) {
                return (String) response.get("optimizedContent");
            }

        } catch (Exception e) {
            log.error("Failed to optimize content via AI service: {}", e.getMessage());
        }

        return applyBasicOptimization(content, optimizationGoal);
    }

    @Override
    public double calculateRelevanceScore(String content, String prompt) {
        if (content == null || prompt == null) {
            return 0.0;
        }

        String[] promptWords = prompt.toLowerCase().split("\\s+");
        String[] contentWords = content.toLowerCase().split("\\s+");

        Set<String> promptWordSet = new HashSet<>(Arrays.asList(promptWords));
        Set<String> contentWordSet = new HashSet<>(Arrays.asList(contentWords));

        int matchingWords = 0;
        for (String word : promptWordSet) {
            if (contentWordSet.contains(word)) {
                matchingWords++;
            }
        }

        return promptWordSet.isEmpty() ? 0.5 : (double) matchingWords / promptWordSet.size();
    }

    private Map<String, Object> buildGenerationRequest(String prompt, ContentType type,
                                                       ContentTone tone, String language) {
        Map<String, Object> request = new HashMap<>();
        request.put("prompt", prompt);
        request.put("contentType", type.toString());
        request.put("tone", tone != null ? tone.toString() : "neutral");
        request.put("language", language != null ? language : "en");
        request.put("maxTokens", getMaxTokensForType(type));
        return request;
    }

    private int getMaxTokensForType(ContentType type) {
        return switch (type) {
            case BLOG_POST -> 1500;
            case PRODUCT_DESCRIPTION -> 300;
            case SOCIAL_MEDIA -> 100;
            case EMAIL -> 500;
            case AD_COPY -> 200;
            case LANDING_PAGE -> 800;
            case FAQ -> 200;
            case REVIEW -> 400;
            case NEWS -> 800;
            case TUTORIAL -> 2000;
        };
    }

    private GeneratedContent generateFallbackContent(String prompt, ContentType type, ContentTone tone) {
        String fallbackContent = generateMockContent(prompt, type);

        return GeneratedContent.builder()
                .contentId(UUID.randomUUID().toString())
                .content(fallbackContent)
                .prompt(prompt)
                .contentType(type)
                .tone(tone)
                .build();
    }

    private List<GeneratedContent> generateFallbackVariations(String prompt, ContentType type,
                                                              ContentTone tone, String language, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateFallbackContent(prompt + " (variation " + (i + 1) + ")", type, tone))
                .collect(Collectors.toList());
    }

    private String generateMockContent(String prompt, ContentType type) {
        return switch (type) {
            case PRODUCT_DESCRIPTION -> "Experience the difference with our premium product. " +
                    "Designed with quality and innovation in mind, this item delivers exceptional value. " +
                    "Perfect for everyday use, it combines style with functionality. " +
                    "Features include durable materials, ergonomic design, and user-friendly operation.";
            case BLOG_POST -> "# The Ultimate Guide\n\n" +
                    "In today's fast-paced world, staying informed is more important than ever. " +
                    "This comprehensive guide explores the key aspects of " + prompt.substring(0, Math.min(50, prompt.length())) + ".\n\n" +
                    "## Key Takeaways\n\n" +
                    "- Understanding the fundamentals\n" +
                    "- Practical applications and benefits\n" +
                    "- Expert insights and recommendations\n\n" +
                    "By following these guidelines, you'll be well-equipped to make informed decisions.";
            case SOCIAL_MEDIA -> "Exciting news! We're thrilled to share this amazing update with you. " +
                    "Don't miss out on this opportunity to transform your experience. " +
                    "Like, share, and tag your friends! #Exciting #NewRelease #MustSee";
            case EMAIL -> "Subject: You Won't Want to Miss This!\n\n" +
                    "Hi there,\n\n" +
                    "We hope this email finds you well. We're excited to share some fantastic news with you.\n\n" +
                    "Our latest offering is designed to help you achieve your goals.\n\n" +
                    "Best regards,\nThe Team";
            case AD_COPY -> "Discover the Difference Today!\n\n" +
                    "Transform your experience with our premium solution. " +
                    "Limited time offer - Act now and save 20%!\n\n" +
                    "Shop Now | Learn More";
            case LANDING_PAGE -> "Welcome to the Future of Innovation\n\n" +
                    "Experience cutting-edge solutions designed with you in mind. " +
                    "Our platform delivers unmatched quality and reliability.\n\n" +
                    "Get Started Today";
            case FAQ -> "Q: What makes this product unique?\n" +
                    "A: Our product stands out due to its innovative design and premium quality.\n\n" +
                    "Q: How do I get started?\n" +
                    "A: Simply sign up and follow our easy setup process.\n\n" +
                    "Q: Is there a guarantee?\n" +
                    "A: Yes, we offer a satisfaction guarantee on all purchases.";
            case REVIEW -> "A Solid Choice for Anyone\n\n" +
                    "After extensive use, I can confidently say this product delivers on its promises. " +
                    "The quality is impressive, and the user experience is seamless.\n\n" +
                    "Pros: Great value, excellent quality, easy to use\n" +
                    "Cons: Limited color options\n\n" +
                    "Overall, I highly recommend this to anyone looking for reliability.";
            case NEWS -> "Breaking: Major Development Announced\n\n" +
                    "In a significant announcement today, industry leaders revealed plans for groundbreaking changes. " +
                    "Experts predict this will have far-reaching implications.\n\n" +
                    "Stay tuned for more updates as this story develops.";
            case TUTORIAL -> "Step-by-Step Tutorial: Getting Started\n\n" +
                    "Introduction:\nThis tutorial will guide you through the essential steps.\n\n" +
                    "Step 1: Begin by preparing your workspace.\n\n" +
                    "Step 2: Follow the on-screen instructions carefully.\n\n" +
                    "Step 3: Verify your progress at each stage.\n\n" +
                    "Conclusion:\nWith these simple steps, you're now ready to proceed confidently.";
            default -> "Generated content based on your prompt: " + prompt;
        };
    }

    private String applyBasicOptimization(String content, String goal) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        String optimized = content;

        if ("conciseness".equalsIgnoreCase(goal)) {
            optimized = content.replaceAll("\\s+", " ").trim();
        }

        if ("readability".equalsIgnoreCase(goal)) {
            optimized = optimized.replaceAll("(?<!\\s)(?=[.!?])\\s*", ". ");
        }

        return optimized;
    }
}
