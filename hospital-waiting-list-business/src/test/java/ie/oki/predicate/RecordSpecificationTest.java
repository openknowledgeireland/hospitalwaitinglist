package ie.oki.predicate;

import ie.oki.config.TestJpaConfig;
import ie.oki.enums.CaseType;
import ie.oki.enums.Classification;
import ie.oki.enums.CsvType;
import ie.oki.enums.SearchOperation;
import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;
import ie.oki.repository.RecordRepository;
import ie.oki.util.TestUtil;
import ie.oki.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * @author Zoltan Toth
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(classes = TestJpaConfig.class)
public class RecordSpecificationTest {

    private TestUtil testUtil;
    private Record record;
    private Record record2;

    @Autowired
    private RecordRepository recordRepository;

    private RecordSpecification recordSpecification;
    private SearchCriteria searchCriteria;

    @Before
    public void setup() {
        testUtil = new TestUtil();

        record = testUtil.createRecord();
        record.setCaseType(CaseType.DAY_CASE);

        recordRepository.save(record);

        record2 = testUtil.createRecord();
        record2.setCaseType(CaseType.INPATIENT);
        record2.setType(CsvType.IPDC);
        record2.setClassification(Classification.ADULT);
        record2.setArchivedDate(Utils.convertStringToDate("2017-01-01"));
        record2.getHospital().getHospitalGroup().setName("hospitalGroupName2");
        record2.getHospital().setName("hospitalName2");
        record2.getSpeciality().setName("specialityName2");

        recordRepository.save(record2);
    }

    @Test
    public void testToPredicate_wrongFieldName() {
        searchCriteria = new SearchCriteria("key", SearchOperation.EQUAL, "value");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToPredicate_wrongFieldName2() {
        searchCriteria = new SearchCriteria("sub.key", SearchOperation.EQUAL, "value");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToPredicate_wrongHospitalSubFieldName() {
        searchCriteria = new SearchCriteria("hospital.hippe", SearchOperation.EQUAL, "value");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToPredicate_wrongHospitalGroupSubFieldName() {
        searchCriteria = new SearchCriteria("hospital.hospitalGroup.namee", SearchOperation.EQUAL, "value");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToPredicate_wrongSpecialitySubFieldName() {
        searchCriteria = new SearchCriteria("speciality.namee", SearchOperation.EQUAL, "value");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToPredicate_lookupByUUID() {
        searchCriteria = new SearchCriteria("id", SearchOperation.EQUAL, record.getId().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByInvalidUUID() {
        searchCriteria = new SearchCriteria("id", SearchOperation.EQUAL, "123123");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByUUID_lessThan() {
        searchCriteria = new SearchCriteria("id", SearchOperation.LESS_THAN, record.getId().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByUUID_greaterThan() {
        searchCriteria = new SearchCriteria("id", SearchOperation.GREATER_THAN, record.getId().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByCaseType() {
        searchCriteria = new SearchCriteria("caseType", SearchOperation.EQUAL, record.getCaseType().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByLowercaseCaseType() {
        searchCriteria = new SearchCriteria("caseType", SearchOperation.EQUAL, record.getCaseType().toString().toLowerCase());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByInvalidCaseType() {
        searchCriteria = new SearchCriteria("caseType", SearchOperation.EQUAL, "sdasdas");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByCaseType_lessThan() {
        searchCriteria = new SearchCriteria("caseType", SearchOperation.LESS_THAN, record.getCaseType().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByCaseType_greaterThan() {
        searchCriteria = new SearchCriteria("caseType", SearchOperation.GREATER_THAN, record.getCaseType().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByClassification() {
        searchCriteria = new SearchCriteria("classification", SearchOperation.EQUAL, record.getClassification().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByLowercaseClassification() {
        searchCriteria = new SearchCriteria("classification", SearchOperation.EQUAL, record.getClassification().toString().toLowerCase());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByInvalidClassification() {
        searchCriteria = new SearchCriteria("classification", SearchOperation.EQUAL, "sdasdas");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByClassification_lessThan() {
        searchCriteria = new SearchCriteria("classification", SearchOperation.LESS_THAN, record.getClassification().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByClassification_greaterThan() {
        searchCriteria = new SearchCriteria("classification", SearchOperation.GREATER_THAN, record.getClassification().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByCsvType() {
        searchCriteria = new SearchCriteria("type", SearchOperation.EQUAL, record.getType().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByLowercaseCsvType() {
        searchCriteria = new SearchCriteria("type", SearchOperation.EQUAL, record.getType().toString().toLowerCase());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByInvalidCsvType() {
        searchCriteria = new SearchCriteria("type", SearchOperation.EQUAL, "sdasdas");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByCsvType_lessThan() {
        searchCriteria = new SearchCriteria("type", SearchOperation.LESS_THAN, record.getType().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByCsvType_greaterThan() {
        searchCriteria = new SearchCriteria("type", SearchOperation.GREATER_THAN, record.getType().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByArchivedDate() {
        searchCriteria = new SearchCriteria("archivedDate", SearchOperation.EQUAL, "2017-01-31");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByInvalidArchivedDate() {
        searchCriteria = new SearchCriteria("archivedDate", SearchOperation.EQUAL, "sdasdas");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    /**
     * It will find both records.
     */
    @Test
    public void testToPredicate_lookupByArchivedDate_lessThan() {
        searchCriteria = new SearchCriteria("archivedDate", SearchOperation.LESS_THAN, "2017-02-01");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(2, result.size());
    }

    @Test
    public void testToPredicate_lookupByArchivedDate_notLessThan() {
        searchCriteria = new SearchCriteria("archivedDate", SearchOperation.LESS_THAN, "2017-01-01");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByArchivedDate_greaterThan() {
        searchCriteria = new SearchCriteria("archivedDate", SearchOperation.GREATER_THAN, "2017-01-15");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
    }

    @Test
    public void testToPredicate_lookupByArchivedDate_notGreaterThan() {
        searchCriteria = new SearchCriteria("archivedDate", SearchOperation.GREATER_THAN, "2017-01-31");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByString() {
        searchCriteria = new SearchCriteria("speciality.name", SearchOperation.EQUAL, record.getSpeciality().getName());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByLowercaseString() {
        searchCriteria = new SearchCriteria("speciality.name", SearchOperation.EQUAL, record.getSpeciality().getName().toLowerCase());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(1, result.size());
        assertEquals(record.getId(), result.get(0).getId());
    }

    @Test
    public void testToPredicate_lookupByInvalidString() {
        searchCriteria = new SearchCriteria("speciality.name", SearchOperation.EQUAL, "sdasdas");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByString_lessThan() {
        searchCriteria = new SearchCriteria("speciality.name", SearchOperation.LESS_THAN, record.getSpeciality().getName());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByString_greaterThan() {
        searchCriteria = new SearchCriteria("speciality.name", SearchOperation.GREATER_THAN, record.getSpeciality().getName());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByNumber() {
        searchCriteria = new SearchCriteria("waiting", SearchOperation.EQUAL, record.getWaiting().toString());
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(2, result.size());
        assertEquals(record.getId(), result.get(0).getId());
        assertEquals(record2.getId(), result.get(1).getId());
    }

    @Test
    public void testToPredicate_lookupByInvalidNumber() {
        searchCriteria = new SearchCriteria("waiting", SearchOperation.EQUAL, "sdasdas");
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByNumber_lessThan() {
        searchCriteria = new SearchCriteria("waiting", SearchOperation.LESS_THAN, String.valueOf(record.getWaiting() + 1));
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(2, result.size());
    }

    @Test
    public void testToPredicate_lookupByNumber_notLessThan() {
        searchCriteria = new SearchCriteria("waiting", SearchOperation.LESS_THAN, String.valueOf(record.getWaiting() - 1));
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

    @Test
    public void testToPredicate_lookupByNumber_greaterThan() {
        searchCriteria = new SearchCriteria("waiting", SearchOperation.GREATER_THAN, String.valueOf(record.getWaiting() - 1));
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(2, result.size());
    }

    @Test
    public void testToPredicate_lookupByNumber_notGreaterThan() {
        searchCriteria = new SearchCriteria("waiting", SearchOperation.GREATER_THAN, String.valueOf(record.getWaiting() + 1));
        recordSpecification = new RecordSpecification(searchCriteria);

        List<Record> result = recordRepository.findAll(recordSpecification);
        assertEquals(0, result.size());
    }

}
