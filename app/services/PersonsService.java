package services;

import dtos.ChoreDto;
import dtos.HomeWorkDto;
import dtos.PersonDto;
import dtos.TaskDto;
import enums.Status;
import exceptions.InvalidPersonException;
import exceptions.InvalidTaskException;
import io.ebean.DuplicateKeyException;
import models.Chore;
import models.HomeWork;
import models.Person;
import play.libs.Json;
import play.mvc.Http;
import utils.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        person.update();
        return PersonConverter.modelToDto(person);
    }

    /**
     * Deletes a person from the system.
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
        try {
            TaskDto task = Json.fromJson(request.body().asJson(), TaskDto.class);
            task.setOwnerId(UUID.fromString(id));
            if (task.getType().equalsIgnoreCase(Constants.CHORE)) {
                Validators.validateAllChoreFieldsPresent((ChoreDto) task);
                return addChore(task);
            }
            else if (task.getType().equalsIgnoreCase(Constants.HOMEWORK)) {
                Validators.validateAllHomeWorkFieldsPresent((HomeWorkDto) task);
                return addHomeWork(task);
            }
            else {
                throw new InvalidTaskException(MsgGenerator.invalidTaskType(task.getType()));
            }
        } catch (Exception e) {
            throw new InvalidTaskException(MsgGenerator.invalidTaskEnum());
        }

    }

    /**
     * Gets the list of tasks which the user owns.
     * @return the list of all owned tasks if status is not specified, the list of tasks in the specified status, or
     * null if no such person exists
     * @throws InvalidTaskException if the specified status is not a valid status
     * */
    public List<TaskDto> getPersonTasks(String id, String status) throws InvalidTaskException {
        if (Person.find.byId(id) == null) {
            return null;
        }
        if (status == null) {
            return getPersonTasksNoStatus(id);
        }
        Status eStatus = Status.getStatusByName(status);
        if (eStatus == null) {
            throw new InvalidTaskException(MsgGenerator.invalidStatus(status));
        }
        return getPersonTasksWithStatus(id, eStatus);
    }



    // #######################
    // Private helper methods
    // #######################

    private List<TaskDto> getPersonTasksNoStatus(String id) {
        // Find all Chore type tasks
        List<Chore> chores = Chore.find.query().where().eq(Constants.OWNER_ID, id).findList();
        //Find all HomeWork type tasks
        List<HomeWork> homeworks = HomeWork.find.query().where().eq(Constants.OWNER_ID, id).findList();
        return Stream.concat(
                TaskConverter.choreListToDtoList(chores).stream(),
                TaskConverter.homeWorkListToDtoList(homeworks).stream()
        ).collect(Collectors.toList());
    }
    private List<TaskDto> getPersonTasksWithStatus(String id, Status status) {
        // Find all Chore type tasks in the specified status
        List<Chore> chores = Chore.find.query().where().eq(Constants.OWNER_ID, id).eq(Constants.STATUS, status).findList();
        //Find all HomeWork type tasks in the specified status
        List<HomeWork> homeworks = HomeWork.find.query().where().eq(Constants.OWNER_ID, id).eq(Constants.STATUS, status).findList();
        return Stream.concat(
                TaskConverter.choreListToDtoList(chores).stream(),
                TaskConverter.homeWorkListToDtoList(homeworks).stream()
            ).collect(Collectors.toList());
    }

    private TaskDto addHomeWork(TaskDto task) {
        HomeWork homeWork = TaskConverter.dtoToHomeWorkModel(task);
        homeWork.insert();
        return TaskConverter.modelToDto(homeWork);
    }

    private TaskDto addChore(TaskDto task) {
        Chore chore = TaskConverter.dtoToChoreModel(task);
        chore.insert();
        return TaskConverter.modelToDto(chore);
    }
}
