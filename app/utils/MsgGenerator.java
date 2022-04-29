package utils;

/**
 * A utility class to generate messages used in the system.
 * */
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

    public static String invalidDate() {
        return "dueDate format must be yyyy-MM-dd.";
    }

    public static String invalidStatus(String status) {
        return String.format("Value '%s' is not a valid task status. Valid values are: Active, Done.", status);
    }

    public static String invalidSize(String size) {
        return String.format("Value '%s' is not a valid chore size. Valid values are: Small, Medium, Large.", size);
    }

    public static String missingStatus() {
        return "status must be specified in the request's body.";
    }

    public static String missingOwnerId() {
        return "ownerId must be specified in the request's body.";
    }

    public static String missingTaskType() {
        return "type must be specified in the request's body.";
    }

    public static String personIdNotFound(String id) {
        return String.format("A person with the id '%s' does not exist.", id);
    }

    public static String taskIdNotFound(String id) {
        return String.format("A task with the id '%s' does not exist.", id);
    }

    public static String invalidTaskType(String type) {
        return String.format("Type '%s' is not a valid task type. Valid types are: Chore, HomeWork.", type);
    }

    public static String noUpdateData() {
        return "Update data must be provided.";
    }

    public static String badFormat() {
        return "Request body format is invalid";
    }
}
