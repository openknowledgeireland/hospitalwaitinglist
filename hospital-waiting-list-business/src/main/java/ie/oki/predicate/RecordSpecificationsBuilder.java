package ie.oki.predicate;

import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The class can build a {@link Specification} which can be used
 * with a {@link org.springframework.stereotype.Repository} to query the database.
 *
 * @author Zoltan Toth
 */
public class RecordSpecificationsBuilder {

    private List<SearchCriteria> params;

    public RecordSpecificationsBuilder() {
        params = new LinkedList<>();
    }

    public RecordSpecificationsBuilder with(final List<SearchCriteria> criteria) {
        params.addAll(criteria);
        return this;
    }

    /**
     * Creates a {@link Specification} using {@link RecordSpecification} and a list of {@link SearchCriteria}.
     * The {@link RecordSpecification#toPredicate(Root, CriteriaQuery, CriteriaBuilder)} function will be called
     * to create a {@link javax.persistence.criteria.Predicate} that will be used to retrieve the data.
     *
     * @return a {@link Specification}
     */
    public Specification<Record> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification<Record>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new RecordSpecification(param));
        }

        Specification<Record> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
