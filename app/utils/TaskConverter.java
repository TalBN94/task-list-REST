package utils;

import dtos.TaskDto;
import enums.Status;
import models.Chore;
import models.HomeWork;
import models.Task;

public class TaskConverter {
    public static HomeWork dtoToHomeWorkModel(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        Status status = taskDto.getStatus() == null ? Status.Active : taskDto.getStatus();
        return new HomeWork(taskDto.getId(), taskDto.getOwnerId(), status, taskDto.getCourse(), taskDto.getDueDate(), taskDto.getDetails());
    }

    public static Chore dtoToChoreModel(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        Status status = taskDto.getStatus() == null ? Status.Active : taskDto.getStatus();
        return new Chore(taskDto.getId(), taskDto.getOwnerId(), status, taskDto.getDescription(), taskDto.getSize());
    }

    public static TaskDto modelToDto(Task task) {
        if (task instanceof Chore) {
            return modelToChoreTaskDto((Chore)task);
        } else {
            return modelToHomeWorkTaskDto((HomeWork)task);
        }
    }

    private static TaskDto modelToChoreTaskDto(Chore chore) {
        return TaskDto.builder()
                .id(chore.getId())
                .ownerId(chore.getOwnerId())
                .status(chore.getStatus())
                .type(Constants.CHORE)
                .description(chore.getDescription())
                .size(chore.getSize())
                .build();
    }

    private static TaskDto modelToHomeWorkTaskDto(HomeWork homeWork) {
        return TaskDto.builder()
                .id(homeWork.getId())
                .ownerId(homeWork.getOwnerId())
                .status(homeWork.getStatus())
                .type(Constants.HOMEWORK)
                .course(homeWork.getCourse())
                .dueDate(homeWork.getDueDate())
                .details(homeWork.getDetails())
                .build();
    }
}
