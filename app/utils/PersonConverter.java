package utils;

import dtos.PersonDto;
import models.Person;

import java.util.List;
import java.util.stream.Collectors;

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

    public static Person dtoToModel(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }
        return new Person(
                personDto.getId(),
                personDto.getName(),
                personDto.getEmail(),
                personDto.getFavoriteProgrammingLanguage());
    }

    public static List<PersonDto> modelListToDtoList(List<Person> persons) {
        return persons.stream().map(PersonConverter::modelToDto).collect(Collectors.toList());
    }
}
