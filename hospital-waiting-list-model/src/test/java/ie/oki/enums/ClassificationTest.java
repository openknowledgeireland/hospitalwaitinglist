package ie.oki.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class ClassificationTest {

    @Test
    public void testGetByValue_nullInput() {
        Classification result = Classification.getByValue(null);

        assertNull(result);
    }

    @Test
    public void testGetByValue_emptyInput() {
        Classification result = Classification.getByValue("");

        assertNull(result);
    }

    @Test
    public void testGetByValue_wrongInput() {
        Classification result = Classification.getByValue("wrong");

        assertNull(result);
    }

    @Test
    public void testGetByValue_success() {
        Classification result = Classification.getByValue("child");

        assertNotNull(result);
        assertEquals(Classification.CHILD, result);
    }
}
