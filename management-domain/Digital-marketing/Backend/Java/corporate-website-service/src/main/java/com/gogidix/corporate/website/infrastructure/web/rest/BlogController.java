package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.application.dto.BlogPostDto;
import com.gogidix.corporate.website.domain.model.BlogPost;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.BlogPostDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Blog", description = "Blog post management and serving API")
public class BlogController {

    private final BlogPostDomainService blogPostDomainService;

    @GetMapping
    @Operation(summary = "Get all published blog posts", description = "Retrieve all published blog posts for a specific region")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved blog posts")
    public ResponseEntity<List<BlogPostDto>> getPublishedPosts(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.getPublishedPostsByRegion(region);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured blog posts", description = "Retrieve featured blog posts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved featured posts")
    public ResponseEntity<List<BlogPostDto>> getFeaturedPosts(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.getFeaturedPosts(region);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent blog posts", description = "Retrieve recent blog posts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recent posts")
    public ResponseEntity<List<BlogPostDto>> getRecentPosts(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Maximum number of posts", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.getRecentPosts(region, limit);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get blog post by ID", description = "Retrieve a specific blog post by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved blog post")
    @ApiResponse(responseCode = "404", description = "Blog post not found")
    public ResponseEntity<BlogPostDto> getBlogPostById(
            @Parameter(description = "Blog post ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language,
            HttpServletRequest request
    ) {
        return blogPostDomainService.getBlogPostById(id)
                .map(post -> {
                    blogPostDomainService.incrementViewCount(id);
                    return ResponseEntity.ok(BlogPostDto.fromEntity(post, language));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get blog post by slug", description = "Retrieve a specific blog post by its slug")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved blog post")
    @ApiResponse(responseCode = "404", description = "Blog post not found")
    public ResponseEntity<BlogPostDto> getBlogPostBySlug(
            @Parameter(description = "Blog post slug", required = true, in = ParameterIn.PATH)
            @PathVariable String slug,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language,
            HttpServletRequest request
    ) {
        return blogPostDomainService.getBlogPostBySlug(slug)
                .map(post -> {
                    blogPostDomainService.incrementViewCount(post.getId());
                    return ResponseEntity.ok(BlogPostDto.fromEntity(post, language));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get blog posts by category", description = "Retrieve blog posts in a specific category")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved blog posts")
    public ResponseEntity<List<BlogPostDto>> getPostsByCategory(
            @Parameter(description = "Category name", required = true, in = ParameterIn.PATH)
            @PathVariable String category,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.getPostsByCategory(category);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Get blog posts by tag", description = "Retrieve blog posts tagged with a specific tag")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved blog posts")
    public ResponseEntity<List<BlogPostDto>> getPostsByTag(
            @Parameter(description = "Tag name", required = true, in = ParameterIn.PATH)
            @PathVariable String tag,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.getPostsByTag(tag);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get blog posts by author", description = "Retrieve blog posts by a specific author")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved blog posts")
    public ResponseEntity<List<BlogPostDto>> getPostsByAuthor(
            @Parameter(description = "Author ID", required = true, in = ParameterIn.PATH)
            @PathVariable String authorId,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.getPostsByAuthor(authorId);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/{id}/related")
    @Operation(summary = "Get related blog posts", description = "Retrieve blog posts related to a specific post")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved related posts")
    public ResponseEntity<List<BlogPostDto>> getRelatedPosts(
            @Parameter(description = "Blog post ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Maximum number of posts", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "5") int limit,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.getRelatedPosts(id, limit);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/search")
    @Operation(summary = "Search blog posts", description = "Search blog posts by keyword")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    public ResponseEntity<List<BlogPostDto>> searchPosts(
            @Parameter(description = "Search keyword", required = true, in = ParameterIn.QUERY)
            @RequestParam String keyword,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<BlogPost> posts = blogPostDomainService.searchBlogPosts(keyword, language);
        List<BlogPostDto> postDtos = posts.stream()
                .map(post -> BlogPostDto.fromEntity(post, language))
                .toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Like a blog post", description = "Increment the like count for a blog post")
    @ApiResponse(responseCode = "200", description = "Successfully liked the post")
    @ApiResponse(responseCode = "404", description = "Blog post not found")
    public ResponseEntity<Void> likePost(
            @Parameter(description = "Blog post ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id
    ) {
        if (blogPostDomainService.getBlogPostById(id).isPresent()) {
            blogPostDomainService.incrementLikeCount(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
