package utils;

import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.TaskDto;
import enums.Status;
import models.Chore;
import models.HomeWork;
import models.Task;

import java.util.List;
import java.util.stream.Collectors;

public class TaskConverter {
    public static HomeWork dtoToHomeWorkModel(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        HomeWorkDto homeWorkDto = (HomeWorkDto)taskDto;
        Status status = homeWorkDto.getStatus() == null ? Status.Active : homeWorkDto.getStatus();
        return new HomeWork(homeWorkDto.getId(), taskDto.getOwnerId(), status, homeWorkDto.getCourse(), homeWorkDto.getDueDate(), homeWorkDto.getDetails());
    }

    public static Chore dtoToChoreModel(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        ChoreDto choreDto = (ChoreDto)taskDto;
        Status status = choreDto.getStatus() == null ? Status.Active : choreDto.getStatus();
        return new Chore(choreDto.getId(), choreDto.getOwnerId(), status, choreDto.getDescription(), choreDto.getSize());
    }

    public static TaskDto modelToDto(Task task) {
        if (task instanceof Chore) {
            return modelToChoreTaskDto((Chore)task);
        } else {
            return modelToHomeWorkTaskDto((HomeWork)task);
        }
    }

    public static List<TaskDto> modelListToDtoList(List<Task> tasks) {
        return tasks.stream().map(TaskConverter::modelToDto).collect(Collectors.toList());
    }

    public static List<TaskDto> choreListToDtoList(List<Chore> chores) {
        return chores.stream().map(TaskConverter::modelToDto).collect(Collectors.toList());
    }

    public static List<TaskDto> homeWorkListToDtoList(List<HomeWork> homeworks) {
        return homeworks.stream().map(TaskConverter::modelToDto).collect(Collectors.toList());
    }

    private static TaskDto modelToChoreTaskDto(Chore chore) {
        return new ChoreDto(
                chore.getId(),
                chore.getOwnerId(),
                chore.getStatus(),
                Constants.CHORE,
                chore.getDescription(),
                chore.getSize()
        );
    }

    private static TaskDto modelToHomeWorkTaskDto(HomeWork homeWork) {
        return new HomeWorkDto(
                homeWork.getId(),
                homeWork.getOwnerId(),
                homeWork.getStatus(),
                Constants.HOMEWORK,
                homeWork.getCourse(),
                homeWork.getDueDate(),
                homeWork.getDetails()
        );
    }
}
