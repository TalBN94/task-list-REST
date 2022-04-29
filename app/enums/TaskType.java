package enums;

import utils.Constants;

public enum TaskType {
    Chore, HomeWork;

    public static TaskType getTaskTypeByName(String name) {
        if (Constants.CHORE.equalsIgnoreCase(name)) {
            return Chore;
        }
        if (Constants.HOMEWORK.equalsIgnoreCase(name)) {
            return HomeWork;
        }
        return null;
    }
}
