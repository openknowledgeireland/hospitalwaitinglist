package ie.oki.repository;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Repository of the Record model.
 *
 * @author Zoltan Toth
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, UUID> {

    @Query("select r from Record r where r.hospital.hipe = :hipe")
    List<Record> findByHospitalHipe(@Param("hipe") int hipe);

    @Query("select r from Record r where r.speciality.hipe = :hipe")
    List<Record> findBySpecialityHipe(@Param("hipe") int hipe);

    boolean existsByArchivedDateAndType(Date archivedDate, CsvType type);
}
