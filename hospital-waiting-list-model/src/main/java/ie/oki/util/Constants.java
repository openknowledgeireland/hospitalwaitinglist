package ie.oki.util;

/**
 * @author Zoltan Toth
 */
public final class Constants {

    public static final String COMMA = ",";
    public static final String HYPHEN = "-";
    public static final String PLUS = "+";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String MONTHS = "Months";

    public static final String PROTOCOL_HTTP = "http";
    public static final String EXTENSION_CSV = "csv";

    public static final String UTF_8 = "UTF-8";

    /**
     * Only from this year on we have the CSV files.
     */
    public static final int CSV_STARTING_YEAR = 2014;

    /**
     * The number of items that have to be in one line for the Outpatient file.
     */
    public static final int LIST_SIZE_OP = 10;

    /**
     * The number of items that have to be in one line for the Inpatient/Outpatient file.
     */
    public static final int LIST_SIZE_IPDC = 11;

    private Constants() {
    }
}
