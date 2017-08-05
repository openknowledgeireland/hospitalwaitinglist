package ie.oki.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class ClassificationTest {

    @Test
    public void testGetEnum_nullInput() {
        Classification result = Classification.getEnum(null);

        assertNull(result);
    }

    @Test
    public void testGetEnum_emptyInput() {
        Classification result = Classification.getEnum("");

        assertNull(result);
    }

    @Test
    public void testGetEnum_wrongInput() {
        Classification result = Classification.getEnum("wrong");

        assertNull(result);
    }

    @Test
    public void testGetEnum_success() {
        Classification result = Classification.getEnum("child");

        assertNotNull(result);
        assertEquals(Classification.CHILD, result);
    }
}
