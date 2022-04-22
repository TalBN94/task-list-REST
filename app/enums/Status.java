package enums;

/**
 * Enum which indicates a task's status
 * */
public enum Status {
    Active, Done;

    public static Status getStatusByName(String name) {
        if (name.equalsIgnoreCase("active")) {
            return Active;
        }
        if (name.equalsIgnoreCase("done")) {
            return Done;
        }
        return null;
    }
}
