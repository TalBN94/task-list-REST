package services;

import dtos.PersonDto;
import exceptions.InvalidPersonException;
import io.ebean.DuplicateKeyException;
import models.Person;
import play.libs.Json;
import play.mvc.Http;
import utils.PersonConverter;

public class PersonsService {

    public PersonDto createPerson(Http.Request request) throws InvalidPersonException {
        // Convert request body to entity
        Person person = Json.fromJson(request.body().asJson(), Person.class);

        // Validate all fields are present
        if (person.getName() == null) {
            throw new InvalidPersonException("name field is missing.");
        }
        if (person.getEmail() == null) {
            throw new InvalidPersonException("email field is missing.");
        }
        if (person.getFavoriteProgrammingLanguage() == null) {
            throw new InvalidPersonException("favoriteProgrammingLanguage field is missing.");
        }

        // Try saving, throw exception if email isn't unique
        try {
            person.save();
            return PersonConverter.modelToDto(person);
        } catch (DuplicateKeyException e) {
            throw new InvalidPersonException(String.format("A person with email '%s' already exists.", person.getEmail()));
        }
    }
}
