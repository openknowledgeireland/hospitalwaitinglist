package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.enums.SearchOperation;
import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;
import ie.oki.repository.RecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Zoltan Toth
 */
@RunWith(MockitoJUnitRunner.class)
public class RecordServiceImplTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordServiceImpl recordServiceImpl;

    private List<Record> records;
    private List<SearchCriteria> searchCriteria;
    private Record record;
    private UUID id;
    private Date archivedDate;
    private CsvType type;

    @Before
    public void setup() {
        records = new ArrayList<>();
        searchCriteria = new ArrayList<>();

        searchCriteria.add(new SearchCriteria("key", SearchOperation.EQUAL, "value"));

        record = new Record();
        id = UUID.randomUUID();
        archivedDate = new Date();
        type = CsvType.OP;

        record.setId(id);
        record.setArchivedDate(archivedDate);

        records.add(record);
    }

    //TODO This test doesn't do much
    @Test
    @SuppressWarnings("unchecked")
    public void testFindByCriteriaList() {
        when(recordRepository.findAll(any(Specification.class))).thenReturn(new ArrayList<>());

        List<Record> result = recordServiceImpl.findByCriteriaList(searchCriteria);

        verify(recordRepository).findAll(any(Specification.class));
        verifyNoMoreInteractions(recordRepository);

        assertNotNull(result);
        assertTrue(result.isEmpty());
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
