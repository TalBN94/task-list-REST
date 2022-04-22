package controllers;

import dtos.PersonDto;
import dtos.TaskDto;
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

/**
 * A class which extends the Controller class and controls REST api calls related to persons.
 * */
public class PersonsController extends Controller {
    private final PersonsService personsService;

    @Inject
    public PersonsController(PersonsService personsService) {
        this.personsService = personsService;
    }

    /**
     * Add a new person to the system.
     * @return 201 status if creation succeeded
     *         400 status if creation failed (with indicative text message)
     * */
    public Result createPerson(Http.Request request) {
        try {
            PersonDto personDto = personsService.createPerson(request);
            return created()
                    .withHeader(Constants.LOCATION_HEADER, "some url") //TODO - verify what should be the URL
                    .withHeader(Constants.CREATED_ID_HEADER, personDto.getId().toString());
        } catch (InvalidPersonException e) {
            return badRequest(e.getMessage());
        }
    }

    /**
     * Get a list of all people in the system.
     * @return 200 status with a JSON containing the list of persons' data
     * */
    public Result getAll() {
        return ok(Json.toJson(personsService.getAll()));
    }

    /**
     * Get a detailed description of the person whose id is provided.
     * @return 200 status with a JSON containing the person's data if the person's id exists
     *         404 status if the user id doesn't exist
     * */
    public Result getPerson(String id) {
        PersonDto personDto = personsService.getPerson(id);
        if (personDto == null) {
            return notFound(MsgGenerator.personIdNotFound(id));
        }
        return ok(Json.toJson(personDto));
    }

    /**
     * Update person details.
     * @return 200 status with the updated person details
     *         404 status if the user id doesn't exist
     *         400 status if update fails with indicative message
     * */
    public Result updatePerson(String id, Http.Request request) {
        try {
            PersonDto updatedPerson = personsService.update(id, Json.fromJson(request.body().asJson(), PersonDto.class));
            if (updatedPerson == null) {
                return notFound(MsgGenerator.personIdNotFound(id));
            }
            return ok(Json.toJson(updatedPerson));
        } catch (InvalidPersonException e) {
            return badRequest(e.getMessage());
        }
    }

    /**
     * Remove the person whose id is provided from the system.
     * @return 200 status if deleted successfully
     *         404 status if the user id doesn't exist
     * */
    public Result deletePerson(String id) {
        if (personsService.delete(id)) {
            return ok();
        } else {
            return notFound(MsgGenerator.personIdNotFound(id));
        }
    }

    /**
     * Get an array of relevant tasks that belong to the person.
     * If the query parameter 'status' is provided, gets only tasks with the specified status.
     * @return 200 status with the list of the relevant tasks
     *         400 status if status is invalid
     *         404 status if the user id doesn't exist
     * */
    public Result getPersonTasks(String id, String status) {
        try {
            List<TaskDto> foundTasks = personsService.getPersonTasks(id, status);
            if (foundTasks != null) {
                return ok(Json.toJson(foundTasks));
            }
            return notFound(MsgGenerator.personIdNotFound(id));
        } catch (InvalidTaskException e) {
            return badRequest(e.getMessage());
        }
    }

    /**
     * Add a new task to the person in the URL.
     * @return 201 status if the task was created successfully
     *         400 status the request is invalid (data fields missing, data makes no sense, data contains illegal values)
     *         404 status if the user id doesn't exist
     * */
    public Result addTask(String id, Http.Request request) {
        try {
            TaskDto taskDto = personsService.addTask(id, request);
            if (taskDto == null) {
                return notFound(MsgGenerator.personIdNotFound(id));
            }
            return created()
                    .withHeader(Constants.LOCATION_HEADER, "some url") //TODO - verify what should be the URL
                    .withHeader(Constants.CREATED_ID_HEADER, taskDto.getId().toString());
        } catch (InvalidTaskException e) {
            return badRequest(e.getMessage());
        }
    }

}
