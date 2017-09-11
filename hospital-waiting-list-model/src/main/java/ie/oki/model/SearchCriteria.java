package ie.oki.model;

import ie.oki.enums.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * It holds the search criteria that comes from the client.
 *
 * @author Zoltan Toth
 */
@Data
@AllArgsConstructor
public class SearchCriteria {

    /**
     * The path to an object in an entity.
     *
     * <p>For example {@code hospital.hipe} when the {@link Record} entity is used
     */
    private String key;

    /**
     * Operation that runs between the key and value.
     */
    private SearchOperation operation;

    /**
     * The value that is compared against the object that's retrieved by the key.
     */
    private String value;


}
