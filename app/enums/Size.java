package enums;

/**
 * Enum which indicates a chore's size
 * */
public enum Size {
    Small, Medium, Large;

    public static Size getSizeByName(String name) {
        if (name == null) {
            return null;
        }
        if (name.equalsIgnoreCase("small")) {
            return Small;
        }
        if (name.equalsIgnoreCase("medium")) {
            return Medium;
        }
        if (name.equalsIgnoreCase("large")) {
            return Large;
        }
        return null;
    }
}
