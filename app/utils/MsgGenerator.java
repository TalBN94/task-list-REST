package utils;

public class MsgGenerator {
    public static String missingField(String fieldName) {
        return String.format("%s field is missing.", fieldName);
    }

    public static String emailExists(String email) {
        return String.format("A person with email '%s' already exists.", email);
    }

    public static String invalidEmail(String email) {
        return String.format("The email address '%s' is invalid.", email);
    }

    public static String userIdNotFound(String id) {
        return String.format("A person with the id '%s' does not exist.", id);
    }

    public static String invalidTaskType(String type) {
        return String.format("Type '%s' is not a valid task type.", type);
    }
}
