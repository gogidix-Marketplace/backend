package com.gogidix.ecommerce.search.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "search_indices")
public class Search extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private String indexName;
    private String queryType;
    private Integer resultCount;

    public Search() {
        this.isActive = true;
        this.priority = 0;
    }

    public Search(String tenantId) {
        super(tenantId);
        this.isActive = true;
        this.priority = 0;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getIndexName() { return indexName; }
    public void setIndexName(String indexName) { this.indexName = indexName; }
    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }
    public Integer getResultCount() { return resultCount; }
    public void setResultCount(Integer resultCount) { this.resultCount = resultCount; }
}
