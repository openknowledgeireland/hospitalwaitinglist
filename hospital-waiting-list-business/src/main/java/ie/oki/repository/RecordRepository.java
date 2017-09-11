package ie.oki.repository;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

/**
 * Repository of the Record model.
 *
 * @author Zoltan Toth
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, UUID>, JpaSpecificationExecutor<Record> {

    boolean existsByArchivedDateAndType(Date archivedDate, CsvType type);
}
