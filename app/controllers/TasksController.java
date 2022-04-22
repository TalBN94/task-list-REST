package controllers;

import dtos.TaskDto;
import dtos.TaskUpdateDto;
import enums.Status;
import exceptions.InvalidTaskException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.TasksService;
import utils.MsgGenerator;

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
    public Result updateTask(String id, Http.Request request) {
            try {
                TaskDto taskDto = tasksService.update(id, Json.fromJson(request.body().asJson(), TaskUpdateDto.class));
                if (taskDto == null) {
                    return notFound(MsgGenerator.taskIdNotFound(id));
                }
                return ok(Json.toJson(taskDto));
            } catch (InvalidTaskException e) {
                return badRequest(e.getMessage());
            } catch (Exception e) {
                return badRequest(MsgGenerator.invalidDate());
            }
    }

    /**
     * Set a task's status.
     * @return 204 status if update was successful
     *         400 status if the requested status is invalid (not active or done)
     *         404 status if the task id doesn't exist
     * */
    public Result updateTaskStatus(String id, Http.Request request) {
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
    public Result updateTaskOwner(String id, Http.Request request) {
        try {
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
        if (tasksService.delete(id)) {
            return ok();
        }
        return notFound(MsgGenerator.taskIdNotFound(id));
    }
}
