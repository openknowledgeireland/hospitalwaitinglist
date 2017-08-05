package ie.oki.service;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;

import java.io.InputStream;

/**
 * @author Zoltan Toth
 */
public interface ProcessFileService {

    /**
     * Parses the entries in the CSV file into {@link Record} and then saves it to the database.
     * Once that's done it also clears the cache, so the next query can retrieve an up to date version.
     *
     * @param inputStream CSV file
     * @param csvType type of CSV
     */
    void readFile(final InputStream inputStream, final CsvType csvType);
}
