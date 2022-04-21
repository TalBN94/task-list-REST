package utils;

import dtos.PersonDto;
import models.Person;

public class PersonConverter {
    public static PersonDto modelToDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .email(person.getEmail())
                .favoriteProgrammingLanguage(person.getFavoriteProgrammingLanguage())
                .build();
    }

    public static Person dtoToModel(PersonDto personDto) {
        return new Person(
                personDto.getId(),
                personDto.getName(),
                personDto.getEmail(),
                personDto.getFavoriteProgrammingLanguage());
    }
}
