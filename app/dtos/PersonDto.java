package dtos;

import lombok.*;

import java.util.UUID;

/**
 * A DTO class to transfer Person data
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PersonDto {
    private UUID id;
    private String name;
    private String email;
    private String favoriteProgrammingLanguage;
}
