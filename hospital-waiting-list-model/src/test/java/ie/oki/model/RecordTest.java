package ie.oki.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class RecordTest {

    private Record record;
    private Date archivedDate;

    @Before
    public void setup() {
        record = new Record();

        archivedDate = new Date();
        record.setArchivedDate(archivedDate);
    }

    @Test
    public void testArchivedDate_notNull() {
        assertNotNull(record.getArchivedDate());
        assertEquals(archivedDate, record.getArchivedDate());
    }

    @Test
    public void testArchivedDate_null() {
        record.setArchivedDate(null);

        assertNull(record.getArchivedDate());
    }
}
