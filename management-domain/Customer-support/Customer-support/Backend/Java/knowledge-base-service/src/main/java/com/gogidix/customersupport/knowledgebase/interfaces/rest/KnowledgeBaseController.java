package com.gogidix.customersupport.knowledgebase.interfaces.rest;

import com.gogidix.customersupport.knowledgebase.domain.model.Article;
import com.gogidix.customersupport.knowledgebase.domain.model.ArticleCategory;
import com.gogidix.customersupport.knowledgebase.domain.model.ArticleTag;
import com.gogidix.customersupport.knowledgebase.application.service.KnowledgeBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/knowledge-base")
@RequiredArgsConstructor
@Tag(name = "Knowledge Base", description = "APIs for managing knowledge base articles and categories")
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @GetMapping("/articles")
    @Operation(summary = "Get all articles", description = "Retrieve all articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(knowledgeBaseService.getAllArticles());
    }

    @GetMapping("/articles/published")
    @Operation(summary = "Get published articles", description = "Retrieve all published articles")
    public ResponseEntity<List<Article>> getPublishedArticles() {
        return ResponseEntity.ok(knowledgeBaseService.getPublishedArticles());
    }

    @GetMapping("/articles/{id}")
    @Operation(summary = "Get article by ID", description = "Retrieve a specific article by ID")
    public ResponseEntity<Article> getArticleById(@PathVariable String id) {
        return ResponseEntity.ok(knowledgeBaseService.getArticleById(id));
    }

    @GetMapping("/articles/slug/{slug}")
    @Operation(summary = "Get article by slug", description = "Retrieve an article by slug")
    public ResponseEntity<Article> getArticleBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(knowledgeBaseService.getArticleBySlug(slug));
    }

    @GetMapping("/articles/category/{categoryId}")
    @Operation(summary = "Get articles by category", description = "Retrieve articles by category")
    public ResponseEntity<List<Article>> getArticlesByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(knowledgeBaseService.getArticlesByCategory(categoryId));
    }

    @GetMapping("/articles/tag/{tag}")
    @Operation(summary = "Get articles by tag", description = "Retrieve articles by tag")
    public ResponseEntity<List<Article>> getArticlesByTag(@PathVariable String tag) {
        return ResponseEntity.ok(knowledgeBaseService.getArticlesByTag(tag));
    }

    @GetMapping("/articles/search")
    @Operation(summary = "Search articles", description = "Search articles by keyword")
    public ResponseEntity<List<Article>> searchArticles(@RequestParam String keyword) {
        return ResponseEntity.ok(knowledgeBaseService.searchArticles(keyword));
    }

    @GetMapping("/articles/featured")
    @Operation(summary = "Get featured articles", description = "Retrieve all featured articles")
    public ResponseEntity<List<Article>> getFeaturedArticles() {
        return ResponseEntity.ok(knowledgeBaseService.getFeaturedArticles());
    }

    @GetMapping("/articles/most-viewed")
    @Operation(summary = "Get most viewed articles", description = "Retrieve most viewed articles")
    public ResponseEntity<List<Article>> getMostViewedArticles() {
        return ResponseEntity.ok(knowledgeBaseService.getMostViewedArticles());
    }

    @GetMapping("/articles/most-helpful")
    @Operation(summary = "Get most helpful articles", description = "Retrieve most helpful articles")
    public ResponseEntity<List<Article>> getMostHelpfulArticles() {
        return ResponseEntity.ok(knowledgeBaseService.getMostHelpfulArticles());
    }

    @PostMapping("/articles")
    @Operation(summary = "Create article", description = "Create a new article")
    public ResponseEntity<Article> createArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String categoryId,
            @RequestParam String authorId,
            @RequestParam String authorName) {
        Article created = knowledgeBaseService.createArticle(title, content, categoryId, authorId, authorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/articles/{id}")
    @Operation(summary = "Update article", description = "Update an existing article")
    public ResponseEntity<Article> updateArticle(
            @PathVariable String id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) String summary,
            @RequestParam String categoryId) {
        return ResponseEntity.ok(knowledgeBaseService.updateArticle(id, title, content, summary, categoryId));
    }

    @PutMapping("/articles/{id}/publish")
    @Operation(summary = "Publish article", description = "Publish an article")
    public ResponseEntity<Article> publishArticle(@PathVariable String id) {
        return ResponseEntity.ok(knowledgeBaseService.publishArticle(id));
    }

    @PutMapping("/articles/{id}/helpful")
    @Operation(summary = "Mark article helpful", description = "Mark an article as helpful or not")
    public ResponseEntity<Article> markArticleHelpful(
            @PathVariable String id,
            @RequestParam boolean helpful) {
        return ResponseEntity.ok(knowledgeBaseService.markArticleHelpful(id, helpful));
    }

    @DeleteMapping("/articles/{id}")
    @Operation(summary = "Delete article", description = "Delete an article")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
        knowledgeBaseService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    public ResponseEntity<List<ArticleCategory>> getAllCategories() {
        return ResponseEntity.ok(knowledgeBaseService.getAllCategories());
    }

    @GetMapping("/categories/active")
    @Operation(summary = "Get active categories", description = "Retrieve all active categories")
    public ResponseEntity<List<ArticleCategory>> getActiveCategories() {
        return ResponseEntity.ok(knowledgeBaseService.getActiveCategories());
    }

    @PostMapping("/categories")
    @Operation(summary = "Create category", description = "Create a new category")
    public ResponseEntity<ArticleCategory> createCategory(
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        ArticleCategory created = knowledgeBaseService.createCategory(name, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/tags")
    @Operation(summary = "Get all tags", description = "Retrieve all tags")
    public ResponseEntity<List<ArticleTag>> getAllTags() {
        return ResponseEntity.ok(knowledgeBaseService.getAllTags());
    }

    @PostMapping("/tags")
    @Operation(summary = "Create tag", description = "Create a new tag")
    public ResponseEntity<ArticleTag> createTag(
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        ArticleTag created = knowledgeBaseService.createTag(name, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
