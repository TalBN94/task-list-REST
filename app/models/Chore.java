package models;

import enums.Size;
import io.ebean.Finder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class Chore extends Task{

    @Column
    @Constraints.Required
    private String description;

    @Column
    @Constraints.Required
    private Size size;

    public static Finder<String, Chore> find = new Finder<>(Chore.class);
}
