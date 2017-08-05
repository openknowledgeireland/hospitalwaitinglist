package ie.oki.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        String temp = input.trim();

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
        return java.sql.Date.valueOf(LocalDate.parse(input, dateTimeFormatter));
    }
}