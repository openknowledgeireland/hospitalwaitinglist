package ie.oki.predicate;

import ie.oki.enums.SearchOperation;
import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Zoltan Toth
 */
public class RecordSpecificationsBuilderTest {

    private List<SearchCriteria> searchCriteriaList;
    private SearchCriteria searchCriteria;
    private String key;
    private SearchOperation operation;
    private String value;

    @Before
    public void setup() {
        key = "key";
        operation = SearchOperation.EQUAL;
        value = "value";

        searchCriteria = new SearchCriteria(key, operation, value);

        searchCriteriaList = new ArrayList<>();
    }

    @Test
    public void testBuild_listEmpty() {
        RecordSpecificationsBuilder builder = new RecordSpecificationsBuilder();
        builder.with(searchCriteriaList);

        Specification<Record> result = builder.build();

        assertNull(result);
    }

    /**
     * Not sure if this test actually does anything at all...
     */
    @Test
    public void testBuild_listOneItem() {
        searchCriteriaList.add(searchCriteria);

        RecordSpecificationsBuilder builder = new RecordSpecificationsBuilder();
        builder.with(searchCriteriaList);

        Specification<Record> result = builder.build();

        assertNotNull(result);
        assertTrue(result instanceof RecordSpecification);
    }
}
