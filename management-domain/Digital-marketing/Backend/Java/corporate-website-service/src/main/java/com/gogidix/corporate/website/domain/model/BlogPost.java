package com.gogidix.corporate.website.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blog_posts")
public class BlogPost {
    @Id
    private String id;

    private String slug;

    @Builder.Default
    private List<LocalizedContent> localizedContent = new ArrayList<>();

    private String featuredImage;
    private String featuredImageAlt;
    private String gallery;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private List<String> categories = new ArrayList<>();

    private String authorId;
    private String authorName;
    private String authorAvatar;

    @Builder.Default
    private Set<Region> availableRegions = Set.of(Region.NG, Region.KE, Region.GH, Region.ZA, Region.US);

    private boolean featured;
    private Integer featuredOrder;

    private boolean allowComments;
    private Integer readTimeMinutes;

    private SeoMetadata seoMetadata;

    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime unpublishDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    @Builder.Default
    private List<String> relatedPosts = new ArrayList<>();

    private Long viewCount;
    private Long likeCount;

    private String schemaType;
    private String schemaData;
}
