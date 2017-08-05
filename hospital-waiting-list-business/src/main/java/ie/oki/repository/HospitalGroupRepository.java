package ie.oki.repository;

import ie.oki.model.HospitalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of the HospitalGroup model.
 *
 * @author Zoltan Toth
 */
@Repository
public interface HospitalGroupRepository extends JpaRepository<HospitalGroup, String> {
}
