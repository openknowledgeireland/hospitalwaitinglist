package ie.oki.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class SearchOperationTest {

    @Test
    public void testGetByValue_nullInput() {
        SearchOperation result = SearchOperation.getByValue(null);

        assertNull(result);
    }

    @Test
    public void testGetByValue_emptyInput() {
        SearchOperation result = SearchOperation.getByValue("");

        assertNull(result);
    }

    @Test
    public void testGetByValue_wrongInput() {
        SearchOperation result = SearchOperation.getByValue("sdfdf");

        assertNull(result);
    }

    @Test
    public void testGetByValue_success() {
        SearchOperation result = SearchOperation.getByValue(":");

        assertNotNull(result);
        assertEquals(SearchOperation.EQUAL, result);
    }

}
