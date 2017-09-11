package ie.oki.service;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;

import java.util.Date;
import java.util.List;

/**
 * @author Zoltan Toth
 */
public interface RecordService {

    /**
     * Looks up the records by the search criteria.
     *
     * @param criteriaList list of {@link SearchCriteria}
     * @return list of {@link Record}
     */
    List<Record> findByCriteriaList(List<SearchCriteria> criteriaList);

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
