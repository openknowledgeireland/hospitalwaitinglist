package ie.oki.util;

import org.junit.Test;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class UtilsTest {

    @Test
    public void testIsNullOrEmpty_nullInput() {
        boolean result = Utils.isNullOrEmpty(null);

        assertTrue(result);
    }

    @Test
    public void testIsNullOrEmpty_blankInput() {
        boolean result = Utils.isNullOrEmpty("");

        assertTrue(result);
    }

    @Test
    public void testIsNullOrEmpty_notBlankInput() {
        boolean result = Utils.isNullOrEmpty("test");

        assertFalse(result);
    }

    @Test
    public void testParseInterval_blankInput() {
        List<Integer> result = Utils.parseInterval("");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testParseInterval_noSeparatingChar() {
        List<Integer> result = Utils.parseInterval("test");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * If the input parameter doesn't have numbers in it, then throw a {@link NumberFormatException}
     */
    @Test(expected = NumberFormatException.class)
    public void testParseInterval_notNumbers() {
        Utils.parseInterval("a-b");
    }

    @Test
    public void testParseInterval_bothParamsAreNumbers() {
        List<Integer> result = Utils.parseInterval("1-2");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).longValue());
        assertEquals(2, result.get(1).longValue());
    }

    @Test
    public void testParseInterval_inputHasPlusSign() {
        List<Integer> result = Utils.parseInterval("1+");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).longValue());
        assertNull(result.get(1));
    }

    @Test
    public void testParseTimeBands_nullInput() {
        List<Integer> result = Utils.parseTimeBands(null);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testParseTimeBands_blankInput() {
        List<Integer> result = Utils.parseTimeBands("");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testParseTimeBands_correctTimeBand() {
        List<Integer> result = Utils.parseTimeBands("9-12 Months");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(9, result.get(0).longValue());
        assertEquals(12, result.get(1).longValue());
    }

    @Test
    public void testParseTimeBands_correctMinimumTime() {
        List<Integer> result = Utils.parseTimeBands("18+ Months");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(18, result.get(0).longValue());
        assertNull(result.get(1));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseTimeBands_wrongFormat() {
        Utils.parseTimeBands("18+ months");
    }

    @Test
    public void testConvertStringToDate_success() {
        Date result = Utils.convertStringToDate("2017-02-01");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);

        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test(expected = DateTimeParseException.class)
    public void testConvertStringToDate_emptyInput() {
        Utils.convertStringToDate("");
    }

    @Test(expected = NullPointerException.class)
    public void testConvertStringToDate_nullInput() {
        Utils.convertStringToDate(null);
    }

    @Test
    public void testNormalizeAndTrimList_nullInput() {
        List<String> result = Utils.normalizeAndTrimList(null);

        assertNull(result);
    }

    @Test
    public void testNormalizeAndTrimList_emptyInput() {
        List<String> result = Utils.normalizeAndTrimList(new ArrayList<>());

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testNormalizeAndTrimList_nullItemInList() {
        List<String> input = new ArrayList<>();
        input.add(null);
        input.add(" \" test a  aa \"  ");

        List<String> result = Utils.normalizeAndTrimList(input);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(0));
        assertEquals("test a  aa", result.get(1));
    }

    @Test
    public void testNormalizeAndTrimList_notNullItemsInList() {
        List<String> input = new ArrayList<>();
        input.add("test2");
        input.add(" \" test a  aa \"  ");

        List<String> result = Utils.normalizeAndTrimList(input);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test2", result.get(0));
        assertEquals("test a  aa", result.get(1));
    }

    @Test
    public void testNormalizeAndTrim_nullInput() {
        String result = Utils.normalizeAndTrim(null);

        assertNull(result);
    }

    @Test
    public void testNormalizeAndTrim_blankInput() {
        String result = Utils.normalizeAndTrim("");

        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testNormalizeAndTrim_withoutQuotation() {
        String result = Utils.normalizeAndTrim("     test ");

        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test
    public void testNormalizeAndTrim_withQuotation() {
        String result = Utils.normalizeAndTrim("  \"   \"test \"   ");

        assertNotNull(result);
        assertEquals("test", result);
    }
}
