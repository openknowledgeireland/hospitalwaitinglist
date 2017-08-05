package ie.oki.service;

import ie.oki.model.Hospital;

import java.util.List;

/**
 * @author Zoltan Toth
 */
public interface HospitalService {

    /**
     * Returns with all the hospitals.
     *
     * @return list of hospitals
     */
    List<Hospital> findAll();
}
