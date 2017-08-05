package ie.oki.service.impl;

import ie.oki.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * The service exposes functions that are generic.
 *
 * @author Zoltan Toth
 */
@Service
@CacheConfig(cacheNames = "commonService")
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void clearAllCaches() {
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
    }
}
