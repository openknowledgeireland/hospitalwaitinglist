package ie.oki.repository;

import ie.oki.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of the Speciality model.
 *
 * @author Zoltan Toth
 */
@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Integer> {
}
