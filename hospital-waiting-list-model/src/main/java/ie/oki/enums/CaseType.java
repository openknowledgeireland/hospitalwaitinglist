package ie.oki.enums;

import ie.oki.util.Utils;

/**
 * @author Zoltan Toth
 */
public enum CaseType {
    DAY_CASE("Day Case"),
    INPATIENT("Inpatient");

    private final String value;

    CaseType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static CaseType getByValue(final String input) {
        if (Utils.isNullOrEmpty(input)) {
            return null;
        }

        for (CaseType caseType : values()) {
            if (input.equalsIgnoreCase(caseType.getValue())) {
                return caseType;
            }
        }

        return null;
    }
}
