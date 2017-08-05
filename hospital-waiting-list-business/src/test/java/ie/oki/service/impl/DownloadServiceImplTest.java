package ie.oki.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Zoltan Toth
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DownloadServiceImpl.class, InputStream.class, URI.class, URL.class})
public class DownloadServiceImplTest {

    private DownloadServiceImpl downloadServiceImpl;

    private URI uri;
    private URL url;
    private InputStream inputStream;
    private String protocol;
    private String host;
    private String path;

    @Before
    public void setup() {
        downloadServiceImpl = new DownloadServiceImpl();

        uri = PowerMockito.mock(URI.class);
        url = PowerMockito.mock(URL.class);
        inputStream = PowerMockito.mock(InputStream.class);

        protocol = "http";
        host = "host";
        path = "/path";
    }

    @Test
    public void testDownloadFile_success() throws Exception {
        whenNew(URI.class)
            .withArguments(protocol, host, path, null)
            .thenReturn(uri);

        when(uri.toURL()).thenReturn(url);
        when(url.openStream()).thenReturn(inputStream);

        InputStream result = downloadServiceImpl.downloadFile(protocol, host, path);

        assertNotNull(result);

        verifyStatic();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDownloadFile_ioException() throws Exception {
        whenNew(URI.class)
            .withArguments(protocol, host, path, null)
            .thenReturn(uri);

        when(uri.toURL()).thenReturn(url);
        when(url.openStream()).thenThrow(IOException.class);

        InputStream result = downloadServiceImpl.downloadFile(protocol, host, path);

        assertNull(result);

        verifyStatic();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDownloadFile_uriSyntaxException() throws Exception {
        whenNew(URI.class)
            .withArguments(protocol, host, path, null)
            .thenReturn(uri);

        when(uri.toURL()).thenReturn(url);
        when(url.openStream()).thenThrow(URISyntaxException.class);

        InputStream result = downloadServiceImpl.downloadFile(protocol, host, path);

        assertNull(result);

        verifyStatic();
    }
}
