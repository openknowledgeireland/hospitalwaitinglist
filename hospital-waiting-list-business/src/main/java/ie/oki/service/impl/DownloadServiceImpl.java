package ie.oki.service.impl;

import ie.oki.model.UriComponents;
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
    public InputStream downloadFile(final UriComponents uriComponents) {
        try {
            URI uri = new URI(uriComponents.getScheme(), uriComponents.getHost(), uriComponents.getPath(), null);
            return uri.toURL().openStream();
        } catch (IOException | URISyntaxException exc) {
            if (log.isInfoEnabled()) {
                log.info("Couldn't download the file. Input parameters:"
                    + "protocol[" + uriComponents.getScheme() + "]; host[" + uriComponents.getHost() + "]; "
                    + "path[" + uriComponents.getPath() + "].", exc);
            }
        }
        return null;
    }
}
