package ie.oki.service.impl;

import ie.oki.model.Hospital;
import ie.oki.repository.HospitalRepository;
import ie.oki.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * This is responsible for retrieving the hospital data.
 *
 * @author Zoltan Toth
 */
@Service
@CacheConfig(cacheNames = "hospitalService")
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    @Cacheable(key = "#root.methodName")
    public List<Hospital> findAll() {
        List<Hospital> result = hospitalRepository.findAll();

        result.sort(Comparator.comparing(Hospital::getName));

        return result;
    }

}
