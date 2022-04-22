package utils;

import dtos.PersonDto;
import models.Person;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class with static methods to convert person objects.
 * */
public class PersonConverter {
    public static PersonDto modelToDto(Person person) {
        if (person == null) {
            return null;
        }
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .email(person.getEmail())
                .favoriteProgrammingLanguage(person.getFavoriteProgrammingLanguage())
                .build();
    }

    public static List<PersonDto> modelListToDtoList(List<Person> persons) {
        return persons.stream().map(PersonConverter::modelToDto).collect(Collectors.toList());
    }
}
