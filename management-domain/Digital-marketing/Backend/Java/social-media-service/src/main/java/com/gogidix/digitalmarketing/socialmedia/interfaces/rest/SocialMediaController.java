package com.gogidix.digitalmarketing.socialmedia.interfaces.rest;

import com.gogidix.digitalmarketing.socialmedia.application.service.SocialPostService;
import com.gogidix.digitalmarketing.socialmedia.domain.model.SocialPost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/social/posts")
@RequiredArgsConstructor
public class SocialMediaController {
    private final SocialPostService service;

    @PostMapping
    public ResponseEntity<SocialPost> create(@RequestBody SocialPost entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<SocialPost>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialPost> getById(@PathVariable String id) {
        SocialPost result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocialPost> update(@PathVariable String id, @RequestBody SocialPost entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
