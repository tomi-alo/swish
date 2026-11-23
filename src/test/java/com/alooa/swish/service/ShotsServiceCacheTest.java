package com.alooa.swish.service;

import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.mapper.ShotsMapper;
import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.model.Shots;
import com.alooa.swish.repository.ShotsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Checks the cache annotations on ShotsServiceImpl.
 * Uses a tiny Spring context (caching needs Spring's proxy) with an in-memory
 * cache instead of Redis and a Mockito fake repository, so no database or Redis is needed.
 */
@SpringJUnitConfig
class ShotsServiceCacheTest {

    @Configuration
    @EnableCaching
    static class TestConfig {
        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("shots");
        }

        @Bean
        ShotsRepository shotsRepository() {
            return mock(ShotsRepository.class);
        }

        @Bean
        ShotsService shotsService(ShotsRepository shotsRepository) {
            return new ShotsServiceImpl(shotsRepository, new ShotsMapper());
        }
    }

    @Autowired
    private ShotsService shotsService;

    @Autowired
    private ShotsRepository shotsRepository;

    @Autowired
    private CacheManager cacheManager;

    private final Shots shot = new Shots(Category.THREE, Location.LEFT_CORNER, 20, 13, "Corner threes", null);

    @BeforeEach
    void setUp() {
        Mockito.reset(shotsRepository);
        cacheManager.getCache("shots").clear();
        when(shotsRepository.findById(1L)).thenReturn(Optional.of(shot));
    }

    @Test
    void secondReadShouldComeFromCache() {
        shotsService.findById(1L);
        shotsService.findById(1L);

        verify(shotsRepository, times(1)).findById(1L);
    }

    @Test
    void deleteShouldEvictCachedShot() {
        shotsService.findById(1L);
        shotsService.deleteById(1L);
        shotsService.findById(1L);

        verify(shotsRepository, times(2)).findById(1L);
    }

    @Test
    void updateShouldPutFreshShotInCache() {
        when(shotsRepository.save(any(Shots.class))).thenAnswer(invocation -> invocation.getArgument(0));

        shotsService.findById(1L);
        shotsService.updateShots(1L, new UpdateShotsRequest(null, null, null, null, "Updated notes", null));
        ShotsResponse afterUpdate = shotsService.findById(1L);

        // the read after the update comes from the cache and already has the new notes
        assertEquals("Updated notes", afterUpdate.notes());
        // 1st read + the read inside updateShots; the last read was a cache hit
        verify(shotsRepository, times(2)).findById(1L);
    }
}
