package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;
import ie.oki.service.CommonService;
import ie.oki.util.Constants;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * The service exposes functions that are generic.
 *
 * @author Zoltan Toth
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Setter(AccessLevel.PACKAGE)
    @Value("${app.config.csv.host}")
    private String host;

    @Setter(AccessLevel.PACKAGE)
    @Value("${app.config.csv.base-url}")
    private String baseUrl;

    @Setter(AccessLevel.PACKAGE)
    @Value("${app.config.csv.OPFileName}")
    private String opFileName;

    @Setter(AccessLevel.PACKAGE)
    @Value("${app.config.csv.IPDCFileName}")
    private String ipdcFileName;

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
        uriComponents.setHost(host);

        String url = baseUrl;

        if (CsvType.OP.equals(csvType)) {
            url += opFileName;
        } else {
            url += ipdcFileName;
        }

        url += " " + Integer.toString(year) + "." + Constants.EXTENSION_CSV;

        uriComponents.setPath(url);

        return uriComponents;
    }
}
