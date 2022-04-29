package services;

import com.fasterxml.jackson.databind.JsonNode;
import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.PersonDto;
import dtos.TaskDto;
import enums.Status;
import enums.TaskType;
import exceptions.InvalidPersonException;
import exceptions.InvalidTaskException;
import io.ebean.DuplicateKeyException;
import models.Person;
import models.Task;
import play.libs.Json;
import play.mvc.Http;
import utils.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A service class for Person which implements the server logic and interaction with DB
 * */
public class PersonsService {

    /**
     * Creates a new person in the system.
     * @return the newly created person data
     * @throws DuplicateKeyException if the email already exists in the system
     * @throws InvalidPersonException if some fields are missing or data is not as expected
     * */
    public PersonDto createPerson(Http.Request request) throws InvalidPersonException {
        // Convert request body to entity
        Person person = Json.fromJson(request.body().asJson(), Person.class);

        // Validate all fields are present
        Validators.validateAllPersonFieldsPresent(person);

        // Try saving, throw exception if email isn't unique
        try {
            person.insert();
            return PersonConverter.modelToDto(person);
        } catch (DuplicateKeyException e) {
            throw new InvalidPersonException(MsgGenerator.emailExists(person.getEmail()));
        }
    }

    /**
     * Gets all the persons from the system.
     * @return A list of the persons details
     * */
    public List<PersonDto> getAll() {
        return PersonConverter.modelListToDtoList(Person.find.all());
    }

    /**
     * Gets a person from the system by its id.
     * @return the person's details, or null if the person doesn't exist
     * */
    public PersonDto getPerson(String id) {
        return PersonConverter.modelToDto(Person.find.byId(id));
    }

    /**
     * Updates a person in the system.
     * @return the updated person's details, null if no such person exists
     * @throws InvalidPersonException if the email is invalid
     * */
    public PersonDto update(String id, PersonDto updatePersonDto) throws InvalidPersonException {
        Person person = Person.find.byId(id);

        // If person doesn't exist, return null
        if (person == null) {
            return null;
        }

        // Update name field if present
        person.setName(updatePersonDto.getName() == null ? person.getName() : updatePersonDto.getName());

        // Update email field if present and valid
        if (updatePersonDto.getEmail() != null) {
            if (!Validators.isValidEmail(updatePersonDto.getEmail())) {
                throw new InvalidPersonException(MsgGenerator.invalidEmail(updatePersonDto.getEmail()));
            } else {
                person.setEmail(updatePersonDto.getEmail());
            }
        }

        // Update favoriteProgrammingLanguage if present
        person.setFavoriteProgrammingLanguage(updatePersonDto.getFavoriteProgrammingLanguage() == null ?
                person.getFavoriteProgrammingLanguage() : updatePersonDto.getFavoriteProgrammingLanguage());

        try {
            person.update();
        } catch (DuplicateKeyException e) {
            throw new InvalidPersonException(MsgGenerator.emailExists(person.getEmail()));
        }
        return PersonConverter.modelToDto(person);
    }

    /**
     * Deletes a person from the system (and all of its tasks).
     * @return whether deletion was successful
     * */
    public boolean delete(String id) {
        Person personToDelete = Person.find.byId(id);
        if (personToDelete == null) {
            return false;
        }
        return personToDelete.delete();
    }

    /**
     * Adds a task to the specified person.
     * @return the newly created task, or null if no such person exists
     * @throws InvalidTaskException if the task is invalid
     * */
    public TaskDto addTask(String id, Http.Request request) throws InvalidTaskException {
        Person person = Person.find.byId(id);

        // If no person with supplied id, return null and controller handles response
        if (person == null) {
            return null;
        }
        TaskDto task = TaskConverter.requestToTaskDto(request);
        if (task.getType() != null && task.getType() == TaskType.Chore) {
            ChoreDto choreDto = TaskConverter.requestToChoreDto(request);
            Validators.validateAllChoreFieldsPresent(choreDto);
            return addChore(choreDto, person);
        }
        else if (task.getType() != null && task.getType() == TaskType.HomeWork) {
            JsonNode dueDateField = request.body().asJson().get(Constants.DUE_DATE);
            if (dueDateField != null && !Validators.isValidDate(dueDateField.asText())) {
                throw new InvalidTaskException(MsgGenerator.invalidDate());
            }
            HomeWorkDto homeWorkDto = Json.fromJson(request.body().asJson(), HomeWorkDto.class);
            Validators.validateAllHomeWorkFieldsPresent(homeWorkDto);
            return addHomeWork(homeWorkDto, person);
        } else {
            throw new InvalidTaskException(MsgGenerator.missingTaskType());
        }
    }

    /**
     * Gets the list of tasks which the user owns.
     * @return the list of all owned tasks if status is not specified, the list of tasks in the specified status, or
     * null if no such person exists
     * @throws InvalidTaskException if the specified status is not a valid status
     * */
    public List<TaskDto> getPersonTasks(String id, String status) throws InvalidTaskException {
        Person person = Person.find.byId(id);
        if (person == null) {
            return null;
        }
        if (status == null) {
            return person.getTasks().stream().map(TaskConverter::modelToDto).collect(Collectors.toList());
        }
        Status eStatus = Status.getStatusByName(status);
        if (eStatus == null) {
            throw new InvalidTaskException(MsgGenerator.invalidStatus(status));
        }
        return person.getTasks().stream()
                .filter(task -> task.getStatus() == eStatus)
                .map(TaskConverter::modelToDto)
                .collect(Collectors.toList());
    }

    // #######################
    // Private helper methods
    // #######################
    private TaskDto addHomeWork(HomeWorkDto task, Person owner) {
        Task homeWork = TaskConverter.homeWorkDtoToTaskModel(task, owner);
        homeWork.insert();
        return TaskConverter.modelToDto(homeWork);
    }

    private TaskDto addChore(ChoreDto task, Person owner) {
        Task chore = TaskConverter.choreDtoToTaskModel(task, owner);
        chore.insert();
        return TaskConverter.modelToDto(chore);
    }
}
