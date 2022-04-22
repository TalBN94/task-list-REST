package utils;

import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.TaskDto;
import exceptions.InvalidTaskException;
import org.apache.commons.validator.routines.EmailValidator;

public class Validators {
    public static void validateAllChoreFieldsPresent(ChoreDto task) throws InvalidTaskException {
        if (task.getDescription() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.DESCRIPTION));
        }
    }

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

    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
