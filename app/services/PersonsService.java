package services;

import dtos.PersonDto;
import exceptions.InvalidPersonException;
import io.ebean.DuplicateKeyException;
import models.Person;
import org.apache.commons.validator.routines.EmailValidator;
import play.libs.Json;
import play.mvc.Http;
import utils.Constants;
import utils.MsgGenerator;
import utils.PersonConverter;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

public class PersonsService {

    public PersonDto createPerson(Http.Request request) throws InvalidPersonException {
        // Convert request body to entity
        Person person = Json.fromJson(request.body().asJson(), Person.class);

        // Validate all fields are present
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

        // Try saving, throw exception if email isn't unique
        try {
            person.insert();
            return PersonConverter.modelToDto(person);
        } catch (DuplicateKeyException e) {
            throw new InvalidPersonException(MsgGenerator.emailExists(person.getEmail()));
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

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean delete(String id) {
        Person personToDelete = Person.find.byId(id);
        if (personToDelete == null) {
            return false;
        }
        return personToDelete.delete();
    }
}
