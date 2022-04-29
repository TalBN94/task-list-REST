package utils;

import com.fasterxml.jackson.databind.JsonNode;
import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.TaskDto;
import dtos.TaskUpdateDto;
import enums.Status;
import enums.TaskType;
import exceptions.InvalidTaskException;
import models.Person;
import models.Task;
import play.libs.Json;
import play.mvc.Http;

/**
 * A utility class with static methods to convert task objects.
 * */
public class TaskConverter {

    /**
     * Converts TaskDTO to HomeWork entity.
     * @return HomeWork entity
     * */
    public static Task homeWorkDtoToTaskModel(HomeWorkDto homeWorkDto, Person owner) {
        if (homeWorkDto == null) {
            return null;
        }
        Status status = homeWorkDto.getStatus() == null ? Status.Active : homeWorkDto.getStatus();
        return Task.builder()
                .id(homeWorkDto.getId())
                .type(TaskType.HomeWork)
                .owner(owner)
                .status(status)
                .details(homeWorkDto.getDetails())
                .dueDate(homeWorkDto.getDueDate())
                .course(homeWorkDto.getCourse())
                .build();
    }

    /**
     * Converts TaskDTO to Chore entity.
     * @return Chore entity
     * */
    public static Task choreDtoToTaskModel(ChoreDto choreDto, Person owner) {
        if (choreDto == null) {
            return null;
        }
        Status status = choreDto.getStatus() == null ? Status.Active : choreDto.getStatus();
        return Task.builder()
                .id(choreDto.getId())
                .type(TaskType.Chore)
                .owner(owner)
                .status(status)
                .description(choreDto.getDescription())
                .size(choreDto.getSize())
                .build();
    }

    /**
     * Converts a Task entity to a TaskDto.
     * @return the TaskDTO
     * */
    public static TaskDto modelToDto(Task task) {
        if (task.getType() == TaskType.HomeWork) {
            return modelToHomeWorkTaskDto(task);
        }
        return modelToChoreTaskDto(task);
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

    public static TaskDto requestToTaskDto(Http.Request request) throws InvalidTaskException {
        try {
            return Json.fromJson(request.body().asJson(), TaskDto.class);
        } catch (RuntimeException e) {
            JsonNode status = request.body().asJson().get(Constants.STATUS);
            throw new InvalidTaskException(MsgGenerator.invalidStatus(status.asText()));
        }
    }

    public static ChoreDto requestToChoreDto(Http.Request request) throws InvalidTaskException {
        try {
            return Json.fromJson(request.body().asJson(), ChoreDto.class);
        } catch (RuntimeException e) {
            JsonNode size = request.body().asJson().get(Constants.SIZE);
            if (size == null) {
                throw new InvalidTaskException(MsgGenerator.missingField(Constants.SIZE));
            }
            throw new InvalidTaskException(MsgGenerator.invalidSize(size.asText()));
        }
    }

    ////////////////////
    // Helper functions
    ////////////////////

    private static TaskDto modelToChoreTaskDto(Task chore) {
        return new ChoreDto(
                chore.getId(),
                chore.getOwner().getId(),
                chore.getStatus(),
                chore.getDescription(),
                chore.getSize()
        );
    }

    private static TaskDto modelToHomeWorkTaskDto(Task homework) {
        return new HomeWorkDto(
                homework.getId(),
                homework.getOwner().getId(),
                homework.getStatus(),
                homework.getCourse(),
                homework.getDueDate(),
                homework.getDetails()
        );
    }
}
