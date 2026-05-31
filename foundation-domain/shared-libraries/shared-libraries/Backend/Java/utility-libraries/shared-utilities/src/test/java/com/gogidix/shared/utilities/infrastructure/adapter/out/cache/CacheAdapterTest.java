package com.gogidix.shared.utilities.infrastructure.adapter.out.cache;

import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CacheAdapterTest {

    @Mock private CacheManager cacheManager;
    @Mock private Cache cache;
    private CacheAdapter adapter;

    @BeforeEach
    void setup() {
        when(cacheManager.getCache("utility-cache")).thenReturn(cache);
        adapter = new CacheAdapter(cacheManager);
    }

    @Test
    void getCacheHit() {
        Cache.ValueWrapper wrapper = mock(Cache.ValueWrapper.class);
        UtilityResult<String> cached = UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "result");
        when(wrapper.get()).thenReturn(cached);
        when(cache.get("key1")).thenReturn(wrapper);
        assertNotNull(adapter.get("key1"));
    }

    @Test
    void getCacheMiss() {
        when(cache.get("key1")).thenReturn(null);
        assertNull(adapter.get("key1"));
    }

    @Test
    void getCacheNullCache() {
        when(cacheManager.getCache("utility-cache")).thenReturn(null);
        assertNull(adapter.get("key1"));
    }

    @Test
    void getCacheException() {
        when(cache.get("key1")).thenThrow(new RuntimeException("error"));
        assertNull(adapter.get("key1"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void put() {
        UtilityResult<String> result = UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "result");
        adapter.put("key1", result, 60);
        verify(cache).put(eq("key1"), any());
    }

    @Test
    void putNullCache() {
        when(cacheManager.getCache("utility-cache")).thenReturn(null);
        UtilityResult<String> result = UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "result");
        assertDoesNotThrow(() -> adapter.put("key1", result, 60));
    }

    @Test
    void remove() {
        adapter.remove("key1");
        verify(cache).evict("key1");
    }

    @Test
    void clear() {
        adapter.clear();
        verify(cache).clear();
    }

    @Test
    void existsTrue() {
        Cache.ValueWrapper wrapper = mock(Cache.ValueWrapper.class);
        when(cache.get("key1")).thenReturn(wrapper);
        assertTrue(adapter.exists("key1"));
    }

    @Test
    void existsFalse() {
        when(cache.get("key1")).thenReturn(null);
        assertFalse(adapter.exists("key1"));
    }

    @Test
    void getStatistics() {
        String stats = adapter.getStatistics();
        assertNotNull(stats);
        assertTrue(stats.contains("utility-cache"));
    }

    @Test
    void putException() {
        doThrow(new RuntimeException("cache error")).when(cache).put(any(), any());
        UtilityResult<String> result = UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "result");
        assertDoesNotThrow(() -> adapter.put("key1", result, 60));
    }

    @Test
    void removeNullCache() {
        when(cacheManager.getCache("utility-cache")).thenReturn(null);
        assertDoesNotThrow(() -> adapter.remove("key1"));
    }

    @Test
    void removeException() {
        doThrow(new RuntimeException("error")).when(cache).evict("key1");
        assertDoesNotThrow(() -> adapter.remove("key1"));
    }

    @Test
    void clearNullCache() {
        when(cacheManager.getCache("utility-cache")).thenReturn(null);
        assertDoesNotThrow(() -> adapter.clear());
    }

    @Test
    void clearException() {
        doThrow(new RuntimeException("error")).when(cache).clear();
        assertDoesNotThrow(() -> adapter.clear());
    }

    @Test
    void existsNullCache() {
        when(cacheManager.getCache("utility-cache")).thenReturn(null);
        assertFalse(adapter.exists("key1"));
    }

    @Test
    void existsException() {
        when(cache.get("key1")).thenThrow(new RuntimeException("error"));
        assertFalse(adapter.exists("key1"));
    }

    @Test
    void getStatisticsException() {
        doThrow(new RuntimeException("error")).when(cacheManager).getCache(any());
        assertNotNull(adapter.getStatistics());
    }
}
