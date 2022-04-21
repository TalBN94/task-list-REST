package controllers;

import dtos.PersonDto;
import exceptions.InvalidPersonException;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.PersonsService;

import javax.inject.Inject;

public class PersonsController extends Controller {
    PersonsService personsService;

    @Inject
    public PersonsController(PersonsService personsService) {
        this.personsService = personsService;
    }

    public Result create(Http.Request request) {
        try {
            PersonDto personDto = personsService.createPerson(request);
            return created().withHeader("Location", "some url").withHeader("x-Created-id", personDto.getId().toString());
        } catch (InvalidPersonException e) {
            return badRequest(e.getMessage());
        }
    }

    public Result getAll() {
//        return ok(Json.toJson(DB.find(Person.class).findList()));
        return ok();
    }

    public Result getPerson(String id) {
        return ok();
    }

    public Result getPersonTasks(String id) {
        return ok();
    }

}
