package models;

import io.ebean.Finder;
import io.ebean.Model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An entity class for Person
 * */
@Entity
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

    @Column(nullable = false)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Task> tasks;

    public static Finder<String, Person> find = new Finder<>(Person.class);

    public Person(UUID id, String name, String email, String favoriteProgrammingLanguage, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.favoriteProgrammingLanguage = favoriteProgrammingLanguage;
        this.tasks = new ArrayList<>();
    }
}
