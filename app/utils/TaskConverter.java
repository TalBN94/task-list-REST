package utils;

import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.TaskDto;
import dtos.TaskUpdateDto;
import enums.Status;
import models.Chore;
import models.HomeWork;
import models.Task;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class with static methods to convert task objects.
 * */
public class TaskConverter {

    /**
     * Converts TaskDTO to HomeWork entity.
     * @return HomeWork entity
     * */
    public static HomeWork dtoToHomeWorkModel(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        HomeWorkDto homeWorkDto = (HomeWorkDto)taskDto;
        Status status = homeWorkDto.getStatus() == null ? Status.Active : homeWorkDto.getStatus();
        return new HomeWork(homeWorkDto.getId(), taskDto.getOwnerId(), status, homeWorkDto.getCourse(), homeWorkDto.getDueDate(), homeWorkDto.getDetails());
    }

    /**
     * Converts TaskDTO to Chore entity.
     * @return Chore entity
     * */
    public static Chore dtoToChoreModel(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        ChoreDto choreDto = (ChoreDto)taskDto;
        Status status = choreDto.getStatus() == null ? Status.Active : choreDto.getStatus();
        return new Chore(choreDto.getId(), choreDto.getOwnerId(), status, choreDto.getDescription(), choreDto.getSize());
    }

    /**
     * Converts a Task entity to a TaskDto.
     * @return the TaskDTO
     * */
    public static TaskDto modelToDto(Task task) {
        if (task instanceof Chore) {
            return modelToChoreTaskDto((Chore)task);
        } else {
            return modelToHomeWorkTaskDto((HomeWork)task);
        }
    }

    /**
     * Converts a list of Chore entities to a list of TaskDTOs.
     * @return the list of TaskDTOs
     * */
    public static List<TaskDto> choreListToDtoList(List<Chore> chores) {
        return chores.stream().map(TaskConverter::modelToDto).collect(Collectors.toList());
    }

    /**
     * Converts a list of HomeWork entities to a list of TaskDTOs.
     * @return the list of TaskDTOs
     * */
    public static List<TaskDto> homeWorkListToDtoList(List<HomeWork> homeworks) {
        return homeworks.stream().map(TaskConverter::modelToDto).collect(Collectors.toList());
    }

    /**
     * Converts a TaskUpdateDTO to a ChoreDTO.
     * @return the ChoreDTO
     * */
    public static ChoreDto updateDtoToChoreDto(TaskUpdateDto taskUpdateDto) {
        return new ChoreDto(
                taskUpdateDto.getId(),
                null,
                taskUpdateDto.getStatus(),
                taskUpdateDto.getDescription(),
                taskUpdateDto.getSize()
        );
    }

    /**
     * Converts a TaskUpdateDTO to a HomeWorkDTO.
     * @return the HomeWorkDTO
     * */
    public static HomeWorkDto updateDtoToHomeworkDto(TaskUpdateDto taskUpdateDto) {
        return new HomeWorkDto(
                taskUpdateDto.getId(),
                null,
                taskUpdateDto.getStatus(),
                taskUpdateDto.getCourse(),
                taskUpdateDto.getDueDate(),
                taskUpdateDto.getDetails()
        );
    }

    ////////////////////
    // Helper functions
    ////////////////////

    private static TaskDto modelToChoreTaskDto(Chore chore) {
        return new ChoreDto(
                chore.getId(),
                chore.getOwnerId(),
                chore.getStatus(),
                chore.getDescription(),
                chore.getSize()
        );
    }

    private static TaskDto modelToHomeWorkTaskDto(HomeWork homeWork) {
        return new HomeWorkDto(
                homeWork.getId(),
                homeWork.getOwnerId(),
                homeWork.getStatus(),
                homeWork.getCourse(),
                homeWork.getDueDate(),
                homeWork.getDetails()
        );
    }
}
