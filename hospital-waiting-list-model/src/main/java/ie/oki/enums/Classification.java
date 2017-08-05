package ie.oki.enums;

import ie.oki.util.Utils;

/**
 * Repository of the Hospital model.
 *
 * @author Zoltan Toth
 */
public enum Classification {
    CHILD,
    ADULT;

    public static Classification getEnum(final String input) {
        if (Utils.isNullOrEmpty(input)) {
            return null;
        }

        for (Classification classification : values()) {
            if (input.equalsIgnoreCase(classification.toString())) {
                return classification;
            }
        }

        return null;
    }
}
