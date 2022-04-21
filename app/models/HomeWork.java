package models;

import io.ebean.Finder;
import io.ebean.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class HomeWork extends Task {

    @Column
    @Constraints.Required
    private String course;

    @Column
    @Constraints.Required
    private Date dueDate;

    @Column
    @Constraints.Required
    private String details;

    public static Finder<String, HomeWork> find = new Finder<>(HomeWork.class);
}
