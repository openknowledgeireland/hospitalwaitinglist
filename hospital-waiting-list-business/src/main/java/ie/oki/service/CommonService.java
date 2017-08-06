package ie.oki.service;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;

/**
 * @author Zoltan Toth
 */
public interface CommonService {

    /**
     * Clears down every cache.
     */
    void clearAllCaches();

    /**
     * Constructs a {@link UriComponents} object using the input parameters,
     * which can be used to download a CSV file.
     *
     * @param csvType type of CSV
     * @param year year
     * @return {@link UriComponents}
     */
    UriComponents constructUriComponents(final CsvType csvType, final int year);
}
