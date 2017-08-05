package ie.oki.service;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;

import java.util.Date;
import java.util.List;

/**
 * @author Zoltan Toth
 */
public interface RecordService {

    Record findById(final String id);

    /**
     * Looks up the records by the hospital hipe number.
     *
     * @param hipe hospital hipe number
     * @return records that match
     */
    List<Record> findByHospitalHipe(final int hipe);

    /**
     * Looks up the records by the speciality hipe number.
     *
     * @param hipe speciality hipe number
     * @return records that match
     */
    List<Record> findBySpecialityHipe(final int hipe);

    /**
     * Checks if the row with the specified input parameters already exists.
     * If there is a row in the database with the same date and {@link CsvType}
     * then we assume that it's processed already and can be skipped.
     *
     * @param date archived date
     * @param type CsvType
     * @return true if there is a row in the database with the archived date
     */
    boolean isRowAlreadyProcessed(final Date date, final CsvType type);

    Record save(final Record record);
}
