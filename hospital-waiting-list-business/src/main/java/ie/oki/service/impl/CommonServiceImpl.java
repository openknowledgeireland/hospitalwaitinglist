package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;
import ie.oki.properties.AppProperties;
import ie.oki.service.CommonService;
import ie.oki.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * The service exposes functions that are generic.
 *
 * @author Zoltan Toth
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void clearAllCaches() {
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
    }

    @Override
    public UriComponents constructUriComponents(final CsvType csvType, final int year) {
        UriComponents uriComponents = new UriComponents();

        uriComponents.setScheme(Constants.PROTOCOL_HTTP);
        uriComponents.setHost(appProperties.getCsv().getHost());

        String url = appProperties.getCsv().getBaseUrl();

        if (CsvType.OP.equals(csvType)) {
            url += appProperties.getCsv().getOpFileName();
        } else {
            url += appProperties.getCsv().getIpdcFileName();
        }

        url += " " + Integer.toString(year) + "." + Constants.EXTENSION_CSV;

        uriComponents.setPath(url);

        return uriComponents;
    }
}
