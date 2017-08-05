package ie.oki.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Zoltan Toth
 */
public class CommonServiceImplTest {

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private CommonServiceImpl commonServiceImpl;

    private List<String> mockCacheNames;
    private List<String> realCacheNames;
    private String cacheName;
    private Cache cache;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        MockitoAnnotations.initMocks(this);

        cache = Mockito.mock(Cache.class);
        cacheName = "cacheName";

        mockCacheNames = Mockito.mock(List.class);
        realCacheNames = new ArrayList<>();

        realCacheNames.add(cacheName);
    }

    @Test
    public void testClearAllCaches() {
        when(cacheManager.getCacheNames()).thenReturn(mockCacheNames);
        when(mockCacheNames.parallelStream()).thenReturn(realCacheNames.parallelStream());
        when(cacheManager.getCache(cacheName)).thenReturn(cache);

        commonServiceImpl.clearAllCaches();

        verify(cacheManager).getCache(cacheName);
        verify(cache).clear();
    }
}
