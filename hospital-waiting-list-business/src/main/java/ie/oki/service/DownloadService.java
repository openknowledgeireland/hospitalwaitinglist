package ie.oki.service;

import ie.oki.model.UriComponents;

import java.io.InputStream;
import java.net.URI;

/**
 * @author Zoltan Toth
 */
public interface DownloadService {

    /**
     * Downloads a file using the parameters within the {@link UriComponents}.
     * <p>Rather than passing the full url to the function, the details are separated
     * so the {@link URI} can parse the special characters in the path properly.</p>
     *
     * @param uriComponents component that has all the details to download a file
     * @return null or an {@link InputStream} based on whether the download was successful or not
     */
    InputStream downloadFile(final UriComponents uriComponents);
}
