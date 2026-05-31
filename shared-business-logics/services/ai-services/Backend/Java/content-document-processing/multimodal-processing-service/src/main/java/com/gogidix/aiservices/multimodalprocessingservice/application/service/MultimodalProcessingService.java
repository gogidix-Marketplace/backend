package com.gogidix.aiservices.multimodalprocessingservice.application.service;

import com.gogidix.aiservices.multimodalprocessingservice.application.dto.request.ProcessMultimodalRequest;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.request.SearchSimilarRequest;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.response.MultimodalProcessingResponse;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.response.SimilarContentResponse;
import com.gogidix.aiservices.multimodalprocessingservice.domain.aggregate.MultimodalContent;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.*;
import com.gogidix.aiservices.multimodalprocessingservice.domain.port.out.MultimodalRepository;
import com.gogidix.aiservices.multimodalprocessingservice.domain.port.out.EmbeddingEnginePort;
import com.gogidix.aiservices.multimodalprocessingservice.domain.policy.MultimodalProcessingPolicy;
import com.gogidix.aiservices.multimodalprocessingservice.shared.exception.MultimodalProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MultimodalProcessingService {
    private final MultimodalRepository repository;
    private final EmbeddingEnginePort embeddingEngine;
    private final MultimodalProcessingPolicy policy;

    public MultimodalProcessingResponse processMultimodal(ProcessMultimodalRequest request, String userId) {
        validateRequest(request);

        List<ContentItem> items = request.contentItems().stream()
                .map(dto -> new ContentItem(dto.modality(), dto.url(), dto.metadata()))
                .collect(Collectors.toList());

        MultimodalContent content = MultimodalContent.create(items, userId);

        // Generate embeddings for each modality
        for (ContentItem item : items) {
            float[] embedding = embeddingEngine.generateEmbedding(item);
            content.addEmbedding(item.modality(), embedding);
        }

        // Generate fused embedding
        content.generateFusedEmbedding();

        // Generate output based on requested format
        OutputFormat format = request.outputFormat() != null
                ? request.outputFormat()
                : OutputFormat.EMBEDDING;

        switch (format) {
            case SUMMARY -> content.setSummary(generateSummary(content));
            case TAGS -> content.setTags(generateTags(content));
            case FULL -> {
                content.setSummary(generateSummary(content));
                content.setTags(generateTags(content));
            }
        }

        content = repository.save(content);

        return toResponse(content);
    }

    public SimilarContentResponse searchSimilar(SearchSimilarRequest request) {
        if (request.queryEmbedding() == null || request.queryEmbedding().length != 768) {
            throw new MultimodalProcessingException("Invalid query embedding");
        }

        List<MultimodalContent> candidates = repository.findAll();

        // Convert double[] to float[] for comparison
        float[] queryEmbeddingFloat = new float[request.queryEmbedding().length];
        for (int i = 0; i < request.queryEmbedding().length; i++) {
            queryEmbeddingFloat[i] = (float) request.queryEmbedding()[i];
        }

        List<MultimodalContent> similar = candidates.stream()
                .filter(c -> c.getFusedEmbedding() != null)
                .filter(c -> calculateSimilarity(queryEmbeddingFloat, c.getFusedEmbedding()) >= request.threshold())
                .sorted((a, b) -> Double.compare(
                        calculateSimilarity(queryEmbeddingFloat, b.getFusedEmbedding()),
                        calculateSimilarity(queryEmbeddingFloat, a.getFusedEmbedding())
                ))
                .limit(request.limit())
                .collect(Collectors.toList());

        return new SimilarContentResponse(similar.stream()
                .map(c -> new SimilarContentResponse.SimilarItem(
                        c.getContentId().toString(),
                        calculateSimilarity(queryEmbeddingFloat, c.getFusedEmbedding())
                ))
                .collect(Collectors.toList()));
    }

    private void validateRequest(ProcessMultimodalRequest request) {
        List<ContentItem> items = request.contentItems().stream()
                .map(dto -> new ContentItem(dto.modality(), dto.url(), dto.metadata()))
                .collect(Collectors.toList());

        if (!policy.isValidItemCount(items)) {
            throw new MultimodalProcessingException("Invalid content item count (max 10)");
        }

        for (ProcessMultimodalRequest.ContentItemDto item : request.contentItems()) {
            if (!policy.isValidUrl(item.url())) {
                throw new MultimodalProcessingException("Invalid URL: " + item.url());
            }
        }
    }

    private String generateSummary(MultimodalContent content) {
        return "Generated summary for " + content.getModalities().size() + " modalities";
    }

    private List<String> generateTags(MultimodalContent content) {
        return List.of("multimodal", "processed");
    }

    private double calculateSimilarity(float[] embedding1, float[] embedding2) {
        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += embedding1[i] * embedding1[i];
            norm2 += embedding2[i] * embedding2[i];
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private MultimodalProcessingResponse toResponse(MultimodalContent content) {
        Map<ContentModality, float[]> embeddings = content.getEmbeddings();

        Map<String, List<Double>> embeddingMap = embeddings.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        e -> {
                            // Convert float[] to List<Double>
                            List<Double> list = new ArrayList<>(e.getValue().length);
                            for (float v : e.getValue()) {
                                list.add((double) v);
                            }
                            return list;
                        }
                ));

        List<Double> fusedList;
        if (content.getFusedEmbedding() != null) {
            fusedList = new ArrayList<>(content.getFusedEmbedding().length);
            for (float v : content.getFusedEmbedding()) {
                fusedList.add((double) v);
            }
        } else {
            fusedList = List.of();
        }

        return new MultimodalProcessingResponse(
                content.getContentId().toString(),
                embeddingMap,
                fusedList,
                content.getSummary(),
                content.getTags()
        );
    }

}
