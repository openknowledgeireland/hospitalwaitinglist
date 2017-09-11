package ie.oki.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ie.oki.util.Constants.*;

/**
 * @author Zoltan Toth
 */
public final class Utils {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private Utils() {
    }

    public static boolean isNullOrEmpty(final String input) {
        return input == null || input.isEmpty();
    }

    public static List<Integer> parseInterval(final String input) {
        List<Integer> result = new LinkedList<>();

        if (isNullOrEmpty(input)) {
            return result;
        }

        String temp = normalizeAndTrim(input);

        if (temp.contains(HYPHEN)) {
            return Arrays.stream(temp.split(HYPHEN)).map(Integer::parseInt).collect(Collectors.toList());
        }

        if (temp.contains(PLUS)) {
            result.add(Integer.parseInt(temp.replace(PLUS, "")));
            // Add this, so when it's retrieved, we don't have to make an exception for the second parameter
            result.add(null);
        }

        return result;
    }

    public static List<Integer> parseTimeBands(final String input) {
        List<Integer> result = new LinkedList<>();

        if (isNullOrEmpty(input)) {
            return result;
        }

        String temp = input.replace(MONTHS, "");

        return parseInterval(temp);
    }

    public static Date convertStringToDate(final String input) {
        if (isNullOrEmpty(input)) {
            return null;
        }

        try {
            return java.sql.Date.valueOf(LocalDate.parse(normalizeAndTrim(input), dateTimeFormatter));
        } catch (DateTimeParseException dtpe) {
            return null;
        }
    }

    /**
     * The items in the list can contain spaces in-front and after the value, furthermore it might have (") character as well.
     * They have to be removed.
     * @param inputList the list which will be normalized
     * @return normalized/fixed list
     */
    public static List<String> normalizeAndTrimList(final List<String> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return inputList;
        }

        return inputList.stream().map(Utils::normalizeAndTrim).collect(Collectors.toList());
    }

    public static String normalizeAndTrim(final String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }

        return input.replace("\"", "").trim();
    }

    public static boolean isValidLookupParam(final String param) {
        if (isNullOrEmpty(param)) {
            return false;
        }

        return param.matches(INPUT_PARAM_PATTERN);
    }

    public static boolean isValidUUID(final String input) {
        if (isNullOrEmpty(input)) {
            return false;
        }

        return input.matches(UUID_PATTERN);
    }
}
