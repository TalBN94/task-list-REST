package controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dtos.TaskDto;
import dtos.TaskUpdateDto;
import enums.Status;
import exceptions.InvalidTaskException;
import org.apache.commons.validator.Msg;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.TasksService;
import utils.MsgGenerator;

import javax.inject.Inject;

public class TasksController extends Controller {
    private final TasksService tasksService;

    @Inject
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    public Result getTask(String id) {
        TaskDto taskDto = tasksService.getTask(id);
        if (taskDto == null) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        return ok(Json.toJson(taskDto));
    }

    public Result getTaskStatus(String id) {
        TaskDto taskDto = tasksService.getTask(id);
        if (taskDto == null) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        return ok(Json.toJson(taskDto.getStatus()));
    }

    public Result getTaskOwner(String id) {
        TaskDto taskDto = tasksService.getTask(id);
        if (taskDto == null) {
            return notFound(MsgGenerator.taskIdNotFound(id));
        }
        return ok(Json.toJson(taskDto.getOwnerId()));
    }

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

    public Result deleteTask(String id) {
        if (tasksService.delete(id)) {
            return ok();
        }
        return notFound(MsgGenerator.taskIdNotFound(id));
    }
}
