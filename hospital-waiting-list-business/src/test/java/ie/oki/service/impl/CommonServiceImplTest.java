package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;
import ie.oki.properties.AppProperties;
import ie.oki.util.Constants;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Zoltan Toth
 */
public class CommonServiceImplTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private AppProperties appProperties;

    @Mock
    private AppProperties.Csv csv;

    @InjectMocks
    private CommonServiceImpl commonServiceImpl;

    private List<String> mockCacheNames;
    private List<String> realCacheNames;
    private String cacheName;
    private Cache cache;

    private String baseUrl;
    private String host;
    private String opFileName;
    private String ipdcFileName;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        MockitoAnnotations.initMocks(this);

        cache = Mockito.mock(Cache.class);
        cacheName = "cacheName";

        mockCacheNames = Mockito.mock(List.class);
        realCacheNames = new ArrayList<>();

        realCacheNames.add(cacheName);

        baseUrl = "baseUrl";
        host = "host";
        opFileName = "opFileName";
        ipdcFileName = "ipdcFileName";
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

    @Test
    public void testConstructUriComponent_op2014() {
        doReturn(csv).when(appProperties).getCsv();
        doReturn(host).when(csv).getHost();
        doReturn(baseUrl).when(csv).getBaseUrl();
        doReturn(opFileName).when(csv).getOpFileName();
        doReturn(ipdcFileName).when(csv).getIpdcFileName();

        UriComponents result = commonServiceImpl.constructUriComponents(CsvType.OP, 2014);

        String expectedBaseUrl = baseUrl + opFileName + " 2014." + Constants.EXTENSION_CSV;

        assertEquals(Constants.PROTOCOL_HTTP, result.getScheme());
        assertEquals(host, result.getHost());
        assertEquals(expectedBaseUrl, result.getPath());

        verify(appProperties, times(3)).getCsv();
        verify(csv).getHost();
        verify(csv).getBaseUrl();
        verify(csv).getOpFileName();

        verifyNoMoreInteractions(appProperties);
        verifyNoMoreInteractions(csv);
    }

    @Test
    public void testConstructUriComponent_ipdc2014() {
        doReturn(csv).when(appProperties).getCsv();
        doReturn(host).when(csv).getHost();
        doReturn(baseUrl).when(csv).getBaseUrl();
        doReturn(ipdcFileName).when(csv).getIpdcFileName();

        UriComponents result = commonServiceImpl.constructUriComponents(CsvType.IPDC, 2014);

        String expectedBaseUrl = baseUrl + ipdcFileName + " 2014." + Constants.EXTENSION_CSV;

        assertEquals(Constants.PROTOCOL_HTTP, result.getScheme());
        assertEquals(host, result.getHost());
        assertEquals(expectedBaseUrl, result.getPath());

        verify(appProperties, times(3)).getCsv();
        verify(csv).getHost();
        verify(csv).getBaseUrl();
        verify(csv).getIpdcFileName();

        verifyNoMoreInteractions(appProperties);
        verifyNoMoreInteractions(csv);
    }
}
