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
import org.apache.commons.validator.routines.EmailValidator;
import play.libs.Json;
import play.mvc.Http;
import utils.Constants;
import utils.MsgGenerator;
import utils.PersonConverter;
import utils.TaskConverter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonsService {

    public PersonDto createPerson(Http.Request request) throws InvalidPersonException {
        // Convert request body to entity
        Person person = Json.fromJson(request.body().asJson(), Person.class);

        // Validate all fields are present
        validateAllPersonFieldsPresent(person);

        // Try saving, throw exception if email isn't unique
        try {
            person.insert();
            return PersonConverter.modelToDto(person);
        } catch (DuplicateKeyException e) {
            throw new InvalidPersonException(MsgGenerator.emailExists(person.getEmail()));
        }
    }

    private void validateAllPersonFieldsPresent(Person person) throws InvalidPersonException {
        if (person.getName() == null) {
            throw new InvalidPersonException(MsgGenerator.missingField(Constants.NAME));
        }
        if (person.getEmail() == null) {
            throw new InvalidPersonException(MsgGenerator.missingField(Constants.EMAIL));
        }
        if (!isValidEmail(person.getEmail())) {
            throw new InvalidPersonException(MsgGenerator.invalidEmail(person.getEmail()));
        }
        if (person.getFavoriteProgrammingLanguage() == null) {
            throw new InvalidPersonException(MsgGenerator.missingField(Constants.FAVORITE_PROGRAMMING_LANGUAGE));
        }
    }

    public List<PersonDto> getAll() {
        return PersonConverter.modelListToDtoList(Person.find.all());
    }

    public PersonDto getPerson(String id) {
        return PersonConverter.modelToDto(Person.find.byId(id));
    }

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
            if (!isValidEmail(updatePersonDto.getEmail())) {
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

    public boolean delete(String id) {
        Person personToDelete = Person.find.byId(id);
        if (personToDelete == null) {
            return false;
        }
        return personToDelete.delete();
    }

    public TaskDto addTask(String id, Http.Request request) throws InvalidTaskException {
        Person person = Person.find.byId(id);

        // If no person with supplied id, return null and controller handles response
        if (person == null) {
            return null;
        }
        try {
            TaskDto task = Json.fromJson(request.body().asJson(), TaskDto.class);
            task.setOwnerId(UUID.fromString(id));
            if (task.getType().equals(Constants.CHORE)) {
                validateAllChoreFieldsPresent(task);
                return addChore(task);
            }
            else if (task.getType().equals(Constants.HOMEWORK)) {
                validateAllHomeWorkFieldsPresent(task);
                return addHomeWork(task);
            }
            else {
                throw new InvalidTaskException(MsgGenerator.invalidTaskType(task.getType()));
            }
        } catch (Exception e) {
            throw new InvalidTaskException(MsgGenerator.invalidTaskEnum());
        }

    }

    public List<TaskDto> getPersonTasks(String id, Status status) {
        if (status == null) {
            return getPersonTasksNoStatus(id);
        }
        return getPersonTasksWithStatus(id, status);
    }



    // #######################
    // Private helper methods
    // #######################

    private List<TaskDto> getPersonTasksNoStatus(String id) {
        List<Chore> chores = Chore.find.query().where().eq(Constants.OWNER_ID, id).findList();
        List<HomeWork> homeworks = HomeWork.find.query().where().eq(Constants.OWNER_ID, id).findList();
        return Stream.concat(
                TaskConverter.choreListToDtoList(chores).stream(),
                TaskConverter.homeWorkListToDtoList(homeworks).stream()
        ).collect(Collectors.toList());
    }
    private List<TaskDto> getPersonTasksWithStatus(String id, Status status) {
        List<Chore> chores = Chore.find.query().where().eq(Constants.OWNER_ID, id).eq(Constants.STATUS, status).findList();
        List<HomeWork> homeworks = HomeWork.find.query().where().eq(Constants.OWNER_ID, id).eq(Constants.STATUS, status).findList();
        return Stream.concat(
                TaskConverter.choreListToDtoList(chores).stream(),
                TaskConverter.homeWorkListToDtoList(homeworks).stream()
            ).collect(Collectors.toList());
    }

    private void validateAllChoreFieldsPresent(TaskDto task) throws InvalidTaskException {
        if (((ChoreDto)task).getDescription() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.DESCRIPTION));
        }
    }

    private void validateAllHomeWorkFieldsPresent(TaskDto task) throws InvalidTaskException {
        if (((HomeWorkDto)task).getCourse() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.COURSE));
        }
        if (((HomeWorkDto)task).getDueDate() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.DUE_DATE));
        }
        if (((HomeWorkDto)task).getDetails() == null) {
            throw new InvalidTaskException(MsgGenerator.missingField(Constants.DETAILS));
        }
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

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
