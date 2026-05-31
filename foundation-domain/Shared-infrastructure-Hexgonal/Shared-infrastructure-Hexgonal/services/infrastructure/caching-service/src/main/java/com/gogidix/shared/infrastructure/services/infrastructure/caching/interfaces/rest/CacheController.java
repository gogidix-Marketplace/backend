package com.gogidix.shared.infrastructure.services.infrastructure.caching.interfaces.rest;

import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.port.in.CachePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * REST controller for cache operations.
 */
@RestController
@RequestMapping("/api/v1/cache")
@Tag(name = "Cache", description = "Cache management APIs")
public class CacheController {

    private final CachePort cachePort;

    public CacheController(CachePort cachePort) {
        this.cachePort = cachePort;
    }

    @PostMapping
    @Operation(summary = "Put value in cache")
    public ResponseEntity<Void> put(
            @RequestParam @NotBlank String key,
            @RequestBody Object value,
            @RequestParam(required = false, defaultValue = "3600") long ttl) {
        cachePort.put(key, value, ttl);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get value from cache")
    public ResponseEntity<Object> get(@PathVariable String key) {
        Object value = cachePort.get(key);
        return ResponseEntity.ok(value != null ? value : Map.of("found", false));
    }

    @DeleteMapping("/{key}")
    @Operation(summary = "Evict value from cache")
    public ResponseEntity<Void> evict(@PathVariable String key) {
        cachePort.evict(key);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "Clear all cache entries")
    public ResponseEntity<Void> clear() {
        cachePort.clear();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    @Operation(summary = "Put multiple values in cache")
    public ResponseEntity<Void> putAll(
            @RequestBody Map<String, Object> entries,
            @RequestParam(required = false, defaultValue = "3600") long ttl) {
        cachePort.putAll(entries, ttl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/get-batch")
    @Operation(summary = "Get multiple values from cache")
    public ResponseEntity<Map<String, Object>> getAll(@RequestBody Set<String> keys) {
        return ResponseEntity.ok(cachePort.getAll(keys));
    }

    @DeleteMapping("/batch")
    @Operation(summary = "Evict multiple values from cache")
    public ResponseEntity<Void> evictAll(@RequestBody Set<String> keys) {
        cachePort.evictAll(keys);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{key}/exists")
    @Operation(summary = "Check if key exists in cache")
    public ResponseEntity<Boolean> exists(@PathVariable String key) {
        return ResponseEntity.ok(cachePort.exists(key));
    }

    @GetMapping("/{key}/ttl")
    @Operation(summary = "Get TTL for a key")
    public ResponseEntity<Long> getTtl(@PathVariable String key) {
        return ResponseEntity.ok(cachePort.getTtl(key));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get cache statistics")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(cachePort.getStats());
    }
}
