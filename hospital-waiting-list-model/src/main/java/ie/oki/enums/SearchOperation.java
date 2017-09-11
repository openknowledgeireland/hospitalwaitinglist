package ie.oki.enums;

import ie.oki.util.Utils;

/**
 * @author Zoltan Toth
 */
public enum SearchOperation {
    EQUAL(":"),
    LESS_THAN("<"),
    GREATER_THAN(">");

    private String value;

    SearchOperation(final String value) {
        this.value = value;
    }

    public static SearchOperation getByValue(final String value) {
        if (Utils.isNullOrEmpty(value)) {
            return null;
        }

        for (SearchOperation operation : SearchOperation.values()) {
            if (value.equals(operation.getValue())) {
                return operation;
            }
        }

        return null;
    }

    public String getValue() {
        return this.value;
    }
}
