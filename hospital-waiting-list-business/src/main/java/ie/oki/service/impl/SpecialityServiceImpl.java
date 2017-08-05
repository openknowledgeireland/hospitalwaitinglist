package ie.oki.service.impl;

import ie.oki.model.Speciality;
import ie.oki.repository.SpecialityRepository;
import ie.oki.service.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * This is responsible for retrieving the speciality data.
 *
 * @author Zoltan Toth
 */
@Service
@CacheConfig(cacheNames = "specialityService")
public class SpecialityServiceImpl implements SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepository;

    @Override
    @Cacheable(key = "#root.methodName")
    public List<Speciality> findAll() {
        List<Speciality> result = specialityRepository.findAll();

        result.sort(Comparator.comparing(Speciality::getName));

        return result;
    }
}
