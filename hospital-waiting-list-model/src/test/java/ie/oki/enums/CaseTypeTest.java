package ie.oki.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class CaseTypeTest {

    @Test
    public void testGetByValue_nullInput() {
        CaseType result = CaseType.getByValue(null);

        assertNull(result);
    }

    @Test
    public void testGetByValue_emptyInput() {
        CaseType result = CaseType.getByValue("");

        assertNull(result);
    }

    @Test
    public void testGetByValue_wrongInput() {
        CaseType result = CaseType.getByValue("wrong");

        assertNull(result);
    }

    @Test
    public void testGetByValue_success() {
        CaseType result = CaseType.getByValue("day case");

        assertNotNull(result);
        assertEquals(CaseType.DAY_CASE, result);
    }
}
