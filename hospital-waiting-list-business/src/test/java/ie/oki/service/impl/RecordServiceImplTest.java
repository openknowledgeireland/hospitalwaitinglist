package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;
import ie.oki.repository.RecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Zoltan Toth
 */
public class RecordServiceImplTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordServiceImpl recordServiceImpl;

    private List<Record> records;
    private String lookupId;
    private Record record;
    private UUID id;
    private UUID id2;
    private Date archivedDate;
    private int hipe;
    private CsvType type;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        records = new ArrayList<>();

        lookupId = UUID.randomUUID().toString();
        record = new Record();
        id = UUID.randomUUID();
        id2 = UUID.randomUUID();
        archivedDate = new Date();
        hipe = 100;
        type = CsvType.OP;

        record.setId(id);
        record.setArchivedDate(archivedDate);

        records.add(record);
    }

    @Test
    public void testFindById_success() {
        when(recordRepository.findOne(UUID.fromString(lookupId))).thenReturn(record);

        Record result = recordServiceImpl.findById(lookupId);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(archivedDate, result.getArchivedDate());

        verify(recordRepository).findOne(UUID.fromString(lookupId));
    }

    @Test
    public void testFindById_noResult() {
        when(recordRepository.findOne(UUID.fromString(lookupId))).thenReturn(null);

        Record result = recordServiceImpl.findById(lookupId);

        assertNull(result);

        verify(recordRepository).findOne(UUID.fromString(lookupId));
    }

    @Test
    public void testFindById_wrongInputParameter() {
        String idInWrongFormat = "test";

        Record result = recordServiceImpl.findById(idInWrongFormat);

        assertNull(result);
    }

    @Test
    public void testFindByHospitalHipe_oneResult() {
        when(recordRepository.findByHospitalHipe(hipe)).thenReturn(records);

        List<Record> result = recordServiceImpl.findByHospitalHipe(hipe);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(archivedDate, result.get(0).getArchivedDate());

        verify(recordRepository).findByHospitalHipe(hipe);
    }

    @Test
    public void testFindByHospitalHipe_twoResults() {
        record = new Record();
        record.setId(id2);

        records.add(record);

        when(recordRepository.findByHospitalHipe(hipe)).thenReturn(records);

        List<Record> result = recordServiceImpl.findByHospitalHipe(hipe);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(archivedDate, result.get(0).getArchivedDate());
        assertEquals(id2, result.get(1).getId());

        verify(recordRepository).findByHospitalHipe(hipe);
    }

    @Test
    public void testFindByHospitalHipe_noResult() {
        records = new ArrayList<>();

        when(recordRepository.findByHospitalHipe(hipe)).thenReturn(records);

        List<Record> result = recordServiceImpl.findByHospitalHipe(hipe);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(recordRepository).findByHospitalHipe(hipe);
    }

    @Test
    public void testFindBySpecialityHipe_oneResult() {
        when(recordRepository.findBySpecialityHipe(hipe)).thenReturn(records);

        List<Record> result = recordServiceImpl.findBySpecialityHipe(hipe);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(archivedDate, result.get(0).getArchivedDate());

        verify(recordRepository).findBySpecialityHipe(hipe);
    }

    @Test
    public void testFindBySpecialityHipe_twoResults() {
        record = new Record();
        record.setId(id2);

        records.add(record);

        when(recordRepository.findBySpecialityHipe(hipe)).thenReturn(records);

        List<Record> result = recordServiceImpl.findBySpecialityHipe(hipe);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(archivedDate, result.get(0).getArchivedDate());
        assertEquals(id2, result.get(1).getId());

        verify(recordRepository).findBySpecialityHipe(hipe);
    }

    @Test
    public void testFindBySpecialityHipe_noResult() {
        records = new ArrayList<>();

        when(recordRepository.findBySpecialityHipe(hipe)).thenReturn(records);

        List<Record> result = recordServiceImpl.findBySpecialityHipe(hipe);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(recordRepository).findBySpecialityHipe(hipe);
    }

    @Test
    public void testIsRowAlreadyProcessed_processed() {
        when(recordRepository.existsByArchivedDateAndType(archivedDate, type)).thenReturn(true);

        boolean result = recordServiceImpl.isRowAlreadyProcessed(archivedDate, type);

        assertTrue(result);

        verify(recordRepository).existsByArchivedDateAndType(archivedDate, type);
    }

    @Test
    public void testIsRowAlreadyProcessed_notProcessed() {
        when(recordRepository.existsByArchivedDateAndType(archivedDate, type)).thenReturn(false);

        boolean result = recordServiceImpl.isRowAlreadyProcessed(archivedDate, type);

        assertFalse(result);

        verify(recordRepository).existsByArchivedDateAndType(archivedDate, type);
    }

    @Test
    public void testSave_success() {
        when(recordRepository.save(record)).thenReturn(record);

        Record result = recordServiceImpl.save(record);

        assertNotNull(result);
        assertEquals(record.getId(), result.getId());
        assertEquals(record.getArchivedDate(), result.getArchivedDate());

        verify(recordRepository).save(record);
    }
}
