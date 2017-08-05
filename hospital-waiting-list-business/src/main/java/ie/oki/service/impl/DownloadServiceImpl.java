package ie.oki.service.impl;

import ie.oki.service.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This service is responsible for downloading files.
 *
 * @author Zoltan Toth
 */
@Slf4j
@Service
public class DownloadServiceImpl implements DownloadService {

    @Override
    public InputStream downloadFile(final String protocol, final String host, final String path) {
        try {
            URI uri = new URI(protocol, host, path, null);
            return uri.toURL().openStream();
        } catch (IOException | URISyntaxException exc) {
            if (log.isInfoEnabled()) {
                log.info("Couldn't download the file. Input parameters: protocol[" + protocol + "]; host[" + host + "]; path[" + path + "].", exc);
            }
        }
        return null;
    }
}
