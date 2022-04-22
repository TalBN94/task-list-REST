package utils;

public class MsgGenerator {
    public static String missingField(String fieldName) {
        return String.format("%s field is missing.", fieldName);
    }

    public static String missingFieldOnTypeChange(String type, String fieldName) {
        return String.format("Error in updating task type to '%s'. %s field is missing.", type, fieldName);
    }

    public static String emailExists(String email) {
        return String.format("A person with email '%s' already exists.", email);
    }

    public static String invalidEmail(String email) {
        return String.format("The email address '%s' is invalid.", email);
    }

    public static String invalidDate() {
        return "Date format must be yyyy-mm-dd.";
    }

    public static String invalidStatus(String status) {
        return String.format("value '%s' is not a legal task status.", status);
    }

    public static String personIdNotFound(String id) {
        return String.format("A person with the id '%s' does not exist.", id);
    }

    public static String taskIdNotFound(String id) {
        return String.format("A task with the id '%s' does not exist.", id);
    }

    public static String invalidTaskType(String type) {
        return String.format("Type '%s' is not a valid task type.", type);
    }

    public static String invalidTaskEnum() {
        return "Some of the fields in the request have invalid values.";
    }
}
