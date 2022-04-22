package models;

import io.ebean.Finder;
import io.ebean.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * An entity class for Person
 * */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person extends Model {

    @Id
    @Column
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String favoriteProgrammingLanguage;

    public static Finder<String, Person> find = new Finder<>(Person.class);

}
