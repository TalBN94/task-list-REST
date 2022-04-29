package enums;

import utils.Constants;

/**
 * Enum which indicates a chore's size
 * */
public enum Size {
    Small, Medium, Large;

    public static Size getSizeByName(String name) {
        if (Constants.SMALL.equalsIgnoreCase(name)) {
            return Small;
        }
        if (Constants.MEDIUM.equalsIgnoreCase(name)) {
            return Medium;
        }
        if (Constants.LARGE.equalsIgnoreCase(name)) {
            return Large;
        }
        return null;
    }
}
