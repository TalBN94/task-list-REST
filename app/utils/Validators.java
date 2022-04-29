package utils;

import dtos.ChoreDto;
import dtos.HomeWorkDto;
import exceptions.InvalidPersonException;
import exceptions.InvalidTaskException;
import models.Person;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.UUID;

/**
 * A utility class with validators used in the server.
 * */
public class Validators {
    /////////////////////
    // Person validators
    ////////////////////
    /**
     * Validates all person's fields are not null
     * @throws InvalidPersonException if a field is null or the email is invalid
     * */
    public static void validateAllPersonFieldsPresent(Person person) throws InvalidPersonException {
        if (person.getName() == null) {
            throw new InvalidPersonException(MsgGenerator.missingField(Constants.NAME));
        }
        if (person.getEmail() == null) {
            throw new InvalidPersonException(MsgGenerator.missingField(Constants.EMAIL));
        }
        if (!Validators.isValidEmail(person.getEmail())) {
            throw new InvalidPersonException(MsgGenerator.invalidEmail(person.getEmail()));
        }
        if (person.getFavoriteProgrammingLanguage() == null) {
            throw new InvalidPersonException(MsgGenerator.missingField(Constants.FAVORITE_PROGRAMMING_LANGUAGE));
        }
    }

    /**
     * Validates an email address
     * @return true if email is valid, false otherwise
     * */
    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Validated a date is in the format of yyyy-MM-dd
     * @return true if date is valid, false otherwise
     * */
    public static boolean isValidDate(String date) {
        return GenericValidator.isDate(date, Constants.DATE_PATTERN, true);
    }

    /**
     * Validates an id is a UUID
     * @return true if the id is a UUID, false otherwise
     * */
    public static boolean isValidId(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    ///////////////////
    // Task validators
    ///////////////////
    /**
     * Validates all chore's fields are not null
     * @throws InvalidTaskException if a field is null
     * */
    public static void validateAllChoreFieldsPresent(ChoreDto task) throws InvalidTaskException {
        if (task.getDescription() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.DESCRIPTION));
        }
        if (task.getSize() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.SIZE));
        }
    }

    /**
     * Validates all homework's fields are not null
     * @throws InvalidTaskException if a field is null
     * */
    public static void validateAllHomeWorkFieldsPresent(HomeWorkDto task) throws InvalidTaskException {
        if (task.getCourse() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.COURSE));
        }
        if (task.getDueDate() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.DUE_DATE));
        }
        if (task.getDetails() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.DETAILS));
        }
    }
}
