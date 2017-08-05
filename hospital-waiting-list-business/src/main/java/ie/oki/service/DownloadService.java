package ie.oki.service;

import java.io.InputStream;
import java.net.URI;

/**
 * @author Zoltan Toth
 */
public interface DownloadService {

    /**
     * Downloads a file using the defined protocol, host and path.
     * <p>Rather than passing the full url to the function, the details are separated
     * so the {@link URI} can parse the special characters in the path properly.</p>
     *
     * @param protocol user either {@code http} or {@code https}
     * @param host the domain of the url, for example: {@code www.example.org}
     * @param path the url of the downloadable file
     * @return null or an {@link InputStream} based on whether the download was successful or not
     */
    InputStream downloadFile(final String protocol, final String host, final String path);
}
