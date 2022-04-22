package controllers;

import dtos.PersonDto;
import dtos.TaskDto;
import enums.Status;
import exceptions.InvalidPersonException;
import exceptions.InvalidTaskException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.PersonsService;
import utils.Constants;
import utils.MsgGenerator;

import javax.inject.Inject;
import java.util.List;

public class PersonsController extends Controller {
    PersonsService personsService;

    @Inject
    public PersonsController(PersonsService personsService) {
        this.personsService = personsService;
    }

    public Result createPerson(Http.Request request) {
        try {
            PersonDto personDto = personsService.createPerson(request);
            return created()
                    .withHeader(Constants.LOCATION_HEADER, "some url") // TODO - verify what should be the URL
                    .withHeader(Constants.CREATED_ID_HEADER, personDto.getId().toString());
        } catch (InvalidPersonException e) {
            return badRequest(e.getMessage());
        }
    }

    public Result getAll() {
        return ok(Json.toJson(personsService.getAll()));
    }

    public Result getPerson(String id) {
        PersonDto personDto = personsService.getPerson(id);
        if (personDto == null) {
            return notFound(MsgGenerator.userIdNotFound(id));
        }
        return ok(Json.toJson(personDto));
    }

    public Result updatePerson(String id, Http.Request request) {
        try {
            PersonDto updatedPerson = personsService.update(id, Json.fromJson(request.body().asJson(), PersonDto.class));
            if (updatedPerson == null) {
                return notFound(MsgGenerator.userIdNotFound(id));
            }
            return ok(Json.toJson(updatedPerson));
        } catch (InvalidPersonException e) {
            return badRequest(e.getMessage());
        }
    }

    public Result deletePerson(String id) {
        if (personsService.delete(id)) {
            return ok();
        } else {
            return badRequest(MsgGenerator.userIdNotFound(id));
        }
    }

    public Result getPersonTasks(String id, String status) {
        List<TaskDto> foundTasks = personsService.getPersonTasks(id, Status.getStatusByName(status));
        return ok(Json.toJson(foundTasks));
    }

    public Result addTask(String id, Http.Request request) {
        try {
            TaskDto taskDto = personsService.addTask(id, request);
            if (taskDto == null) {
                return notFound(MsgGenerator.userIdNotFound(id));
            }
            return created()
                    .withHeader(Constants.LOCATION_HEADER, "some url")
                    .withHeader(Constants.CREATED_ID_HEADER, taskDto.getId().toString());
        } catch (InvalidTaskException e) {
            return badRequest(e.getMessage());
        }
    }

}
