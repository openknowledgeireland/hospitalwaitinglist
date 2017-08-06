package ie.oki.service;

/**
 * @author Zoltan Toth
 */
public interface AutoDownloadService {

    /**
     * Automatically downloads the CSV files from the data.gov.ie site for the current year
     * and checks if all the entries were already processed.
     * If so, then they will be skipped, otherwise they will be inserted to the database.
     */
    void checkFiles();
}
