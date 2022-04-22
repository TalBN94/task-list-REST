package enums;

public enum Status {
    Active, Done;

    public static Status getStatusByName(String name) {
        if (name == null) {
            return null;
        }
        if (name.equalsIgnoreCase("active")) {
            return Active;
        }
        if (name.equalsIgnoreCase("done")) {
            return Done;
        }
        return null;
    }
}
