package ie.oki.repository;

import ie.oki.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of the Hospital model.
 *
 * @author Zoltan Toth
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
}
