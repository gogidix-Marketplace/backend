package com.gogidix.customersupport.knowledgebase.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "kb_tags")
public class ArticleTag extends BaseEntity {

    @Field("name")
    @Indexed(unique = true)
    private String name;

    @Field("slug")
    @Indexed
    private String slug;

    @Field("description")
    private String description;

    @Field("color")
    private String color;

    @Field("usage_count")
    private Integer usageCount;

    public static ArticleTag create(String tenantId, String name) {
        ArticleTag tag = new ArticleTag();
        tag.setId(java.util.UUID.randomUUID().toString());
        tag.setTenantId(tenantId);
        tag.setName(name);
        tag.setSlug(name.toLowerCase().replaceAll("\\s+", "-"));
        tag.setUsageCount(0);
        tag.setCreatedAt(Instant.now());
        tag.setUpdatedAt(Instant.now());
        return tag;
    }

    public void incrementUsage() {
        this.usageCount = (this.usageCount != null ? this.usageCount : 0) + 1;
        this.updateTimestamp();
    }
}
