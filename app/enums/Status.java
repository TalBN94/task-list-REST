package enums;

import utils.Constants;

/**
 * Enum which indicates a task's status
 * */
public enum Status {
    Active, Done;

    public static Status getStatusByName(String name) {
        if (Constants.ACTIVE.equalsIgnoreCase(name)) {
            return Active;
        }
        if (Constants.DONE.equalsIgnoreCase(name)) {
            return Done;
        }
        return null;
    }
}
