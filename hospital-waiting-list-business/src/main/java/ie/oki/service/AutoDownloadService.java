package ie.oki.service;

/**
 * @author Zoltan Toth
 */
public interface AutoDownloadService {

    /**
     * Downloads the CSV files from the data.gov.ie for the current year
     * and checks if they already exists there.
     * If don't then the entries will be inserted to the database.
     */
    void checkFiles();
}
