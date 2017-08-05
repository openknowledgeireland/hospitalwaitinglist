package ie.oki.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class CaseTypeTest {

    @Test
    public void testGetEnumByValue_nullInput() {
        CaseType result = CaseType.getEnumByValue(null);

        assertNull(result);
    }

    @Test
    public void testGetEnumByValue_emptyInput() {
        CaseType result = CaseType.getEnumByValue("");

        assertNull(result);
    }

    @Test
    public void testGetEnumByValue_wrongInput() {
        CaseType result = CaseType.getEnumByValue("wrong");

        assertNull(result);
    }

    @Test
    public void testGetEnumByValue_success() {
        CaseType result = CaseType.getEnumByValue("day case");

        assertNotNull(result);
        assertEquals(CaseType.DAY_CASE, result);
    }
}
