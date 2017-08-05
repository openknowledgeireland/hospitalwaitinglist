package ie.oki.service;

import ie.oki.model.Speciality;

import java.util.List;

/**
 * @author Zoltan Toth
 */
public interface SpecialityService {

    /**
     * Returns with all the specialities.
     *
     * @return list of specialities
     */
    List<Speciality> findAll();
}
