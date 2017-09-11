package ie.oki.predicate;

import ie.oki.enums.CaseType;
import ie.oki.enums.Classification;
import ie.oki.enums.CsvType;
import ie.oki.enums.SearchOperation;
import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;
import ie.oki.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.UUID;

import static ie.oki.util.Constants.*;
import static ie.oki.util.Utils.isValidUUID;

/**
 * This object is used to build a Predicate which then will be used to query the database.
 *
 * @author Zoltan Toth
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RecordSpecification implements Specification<Record> {

    private SearchCriteria criteria;

    RecordSpecification(final SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(final Root<Record> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

        From<Record, Record> from = getFrom(root, criteria.getKey());

        String key = criteria.getKey();

        // If there is a "." in the key, then use only the part that's after the last occurrence,
        // since the entity in the "from" object is already the most inner one.
        if (key.contains(".")) {
            key = key.substring(key.lastIndexOf('.') + 1);
        }

        try {
            Path path = from.get(key);
            return getPredicate(builder, path);
        } catch (IllegalArgumentException iae) {
            if (log.isInfoEnabled()) {
                log.info("Couldn't create a path based on the key[" + key + "], or the criteria value is wrong. Not returning with anything.", iae);
            }
            // In case a proper Predicate can not be constructed, return with false (which will generate zero results)
            return builder.disjunction();
        }
    }

    /**
     * The method will recursively find the most inner {@link From} entity to later on create a {@link Path} using that.
     *
     * @param root the object where the query
     * @param key holds the path to the field that needs to be queried
     * @return an entity which represents either {@link Record}, {@link ie.oki.model.Hospital},
     * {@link ie.oki.model.HospitalGroup} or {@link ie.oki.model.Speciality}
     */
    private From<Record, Record> getFrom(final From<Record, Record> root, final String key) {
        if (Utils.isNullOrEmpty(key) || !key.contains(".")) {
            return root;
        }

        String[] parts = key.split("\\.", 2);

        switch (parts[0]) {
            case HOSPITAL:
                return getFrom(root.join(HOSPITAL), parts[1]);
            case HOSPITAL_GROUP:
                return getFrom(root.join(HOSPITAL_GROUP), parts[1]);
            case SPECIALITY:
                return getFrom(root.join(SPECIALITY), parts[1]);
            default:
                return root;
        }
    }

    /**
     * Builds a {@link Predicate} based on the {@link Path} and the {@code criteria.value}.
     *
     * <p>For the {@link Enum} classes it checks for equality only (no greater or less comparison).
     *
     * <p>But for {@link Date}, {@link String} and the rest of the objects (like {@link Number}), it provides all three options.
     *
     * @param builder creates the {@link Predicate}
     * @param path is used to determine the object type and to compare the {@code criteria.value} with the model attribute
     * @return a {@link Predicate}
     */
    private Predicate getPredicate(final CriteriaBuilder builder, final Path path) {
        if (path.getJavaType().equals(UUID.class)) {
            return processUUID(builder, path);
        } else if (path.getJavaType().equals(CaseType.class)) {
            return processCaseType(builder, path);
        } else if (path.getJavaType().equals(Classification.class)) {
            return processClassification(builder, path);
        } else if (path.getJavaType().equals(CsvType.class)) {
            return processCsvType(builder, path);
        } else if (path.getJavaType().equals(Date.class)) {
            return processDate(builder, path);
        } else if (path.getJavaType().equals(String.class)) {
            return processString(builder, path);
        } else {
            return processDefault(builder, path);
        }
    }

    private Predicate processUUID(final CriteriaBuilder builder, final Path path) {
        if (isValidUUID(criteria.getValue())
            && SearchOperation.EQUAL.equals(criteria.getOperation())) {
            return builder.equal(path, UUID.fromString(criteria.getValue()));
        }

        return builder.disjunction();
    }

    private Predicate processCaseType(final CriteriaBuilder builder, final Path path) {
        CaseType caseType = CaseType.valueOf(criteria.getValue().toUpperCase());

        if (SearchOperation.EQUAL.equals(criteria.getOperation())) {
            return builder.equal(path, caseType);
        }

        return builder.disjunction();
    }

    private Predicate processClassification(final CriteriaBuilder builder, final Path path) {
        Classification classification = Classification.valueOf(criteria.getValue().toUpperCase());

        if (SearchOperation.EQUAL.equals(criteria.getOperation())) {
            return builder.equal(path, classification);
        }

        return builder.disjunction();
    }

    private Predicate processCsvType(final CriteriaBuilder builder, final Path path) {
        CsvType csvType = CsvType.valueOf(criteria.getValue().toUpperCase());

        if (SearchOperation.EQUAL.equals(criteria.getOperation())) {
            return builder.equal(path, csvType);
        }

        return builder.disjunction();
    }

    private Predicate processDate(final CriteriaBuilder builder, final Path path) {
        Date date = Utils.convertStringToDate(criteria.getValue());

        if (SearchOperation.EQUAL.equals(criteria.getOperation())) {
            return builder.equal(path, date);
        } else if (SearchOperation.LESS_THAN.equals(criteria.getOperation())) {
            return builder.lessThan(path, date);
        } else {
            return builder.greaterThan(path, date);
        }
    }

    private Predicate processString(final CriteriaBuilder builder, final Path path) {
        if (SearchOperation.EQUAL.equals(criteria.getOperation())) {
            return builder.equal(builder.lower(path), criteria.getValue().toLowerCase());
        }

        return builder.disjunction();
    }

    private Predicate processDefault(final CriteriaBuilder builder, final Path path) {
        if (SearchOperation.EQUAL.equals(criteria.getOperation())) {
            return builder.equal(path, criteria.getValue());
        } else if (SearchOperation.LESS_THAN.equals(criteria.getOperation())) {
            return builder.lessThan(path, criteria.getValue());
        } else {
            return builder.greaterThan(path, criteria.getValue());
        }
    }
}
