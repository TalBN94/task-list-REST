package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dtos.TaskDto;
import dtos.TaskUpdateDto;
import enums.Size;
import enums.Status;
import enums.TaskType;
import exceptions.InvalidTaskException;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.TasksService;
import utils.Constants;
import utils.MsgGenerator;
import utils.Validators;

import javax.inject.Inject;

/**
 * A class which extends the Controller class and controls REST api calls related to tasks.
 * */
public class TasksController extends Controller {
    private final TasksService tasksService;

    @Inject
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    /**
     * Get the details of the task whose id is provided.
     * @return 200 status with the task's details
     *         404 status if the task id doesn't exist
     * */
    public Result getTask(String id) {
        if (!Validators.isValidId(id)) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        TaskDto taskDto = tasksService.getTask(id);
        if (taskDto == null) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        return ok(Json.toJson(taskDto));
    }

    /**
     * Get the status of the task.
     * @return 200 status with the status of the task
     *         404 status if the task id doesn't exist
     * */
    public Result getTaskStatus(String id) {
        if (!Validators.isValidId(id)) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        TaskDto taskDto = tasksService.getTask(id);
        if (taskDto == null) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        return ok(Json.toJson(taskDto.getStatus()));
    }

    /**
     * Get the task's owner's id.
     * @return 200 status with the owner's id
     *         404 status if the task id doesn't exist
     * */
    public Result getTaskOwner(String id) {
        if (!Validators.isValidId(id)) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        TaskDto taskDto = tasksService.getTask(id);
        if (taskDto == null) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        return ok(Json.toJson(taskDto.getOwnerId()));
    }

    /**
     * Partial updated of task details.
     * @return 200 status with the updated task details
     *         400 status if the request was invalid (invalid fields data, invalid date format)
     *         404 status if the task id doesn't exist
     * */
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateTask(String id, Http.Request request) {
            try {
                if (!Validators.isValidId(id)) {
                    return notFound(MsgGenerator.taskIdNotFound(id));
                }
                JsonNode updateType = request.body().asJson().get(Constants.TYPE);
                if (updateType != null && TaskType.getTaskTypeByName(updateType.asText()) == null) {
                    return badRequest(MsgGenerator.invalidTaskType(updateType.asText()));
                }
                JsonNode updateStatus = request.body().asJson().get(Constants.STATUS);
                if (updateStatus != null && Status.getStatusByName(updateStatus.asText()) == null) {
                    return badRequest(MsgGenerator.invalidStatus(updateStatus.asText()));
                }
                JsonNode updateSize = request.body().asJson().get(Constants.SIZE);
                if (updateSize != null && Size.getSizeByName(updateSize.asText()) == null) {
                    return badRequest(MsgGenerator.invalidSize(updateSize.asText()));
                }
                TaskUpdateDto taskUpdateDto = Json.fromJson(request.body().asJson(), TaskUpdateDto.class);
                if (taskUpdateDto == null) {
                    return badRequest(MsgGenerator.noUpdateData());
                }
                JsonNode dueDateField = request.body().asJson().get(Constants.DUE_DATE);
                if ((taskUpdateDto.getType() == null || taskUpdateDto.getType() == TaskType.HomeWork)
                        && dueDateField != null && !Validators.isValidDate(dueDateField.asText())) {
                    return badRequest(MsgGenerator.invalidDate());
                }

                TaskDto taskDto = tasksService.update(id, taskUpdateDto);
                if (taskDto == null) {
                    return notFound(MsgGenerator.taskIdNotFound(id));
                }
                return ok(Json.toJson(taskDto));
            } catch (InvalidTaskException e) {
                return badRequest(e.getMessage());
            }
    }

    /**
     * Set a task's status.
     * @return 204 status if update was successful
     *         400 status if the requested status is invalid (not active or done)
     *         404 status if the task id doesn't exist
     * */
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateTaskStatus(String id, Http.Request request) {
        if (!Validators.isValidId(id)) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        if (request.body().asJson() == null) {
            return badRequest(MsgGenerator.missingStatus());
        }
        Status status = Status.getStatusByName(request.body().asJson().asText());
        if (status == null) {
            return badRequest(MsgGenerator.invalidStatus(request.body().asJson().asText()));
        }
        if (tasksService.updateTaskStatus(id, status)) {
            return noContent();
        }
        return notFound(MsgGenerator.taskIdNotFound(id));
    }

    /**
     * Set a task's owner.
     * @return 204 status if update was successful
     *         400 status if no such user exists
     *         404 status if the task id doesn't exist
     * */
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateTaskOwner(String id, Http.Request request) {
        try {
            if (!Validators.isValidId(id)) {
                return notFound(MsgGenerator.taskIdNotFound(id));
            }
            if (tasksService.updateTaskOwner(id, Json.fromJson(request.body().asJson(), String.class))) {
                return noContent();
            }
            return notFound(MsgGenerator.taskIdNotFound(id));
        } catch (InvalidTaskException e) {
            return badRequest(e.getMessage());
        } catch (Exception e) {
            return notFound(MsgGenerator.taskIdNotFound(request.body().asJson().asText()));
        }
    }

    /**
     * Remove a task from the system.
     * @return 200 status if deletion was successful
     *         404 status if the task id doesn't exist
     * */
    public Result deleteTask(String id) {
        if (!Validators.isValidId(id)) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        if (tasksService.delete(id)) {
            return ok();
        }
        return notFound(MsgGenerator.taskIdNotFound(id));
    }
}
