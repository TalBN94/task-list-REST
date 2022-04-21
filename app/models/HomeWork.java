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

    @Column(nullable = false)
    private String course;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private String details;

    public static Finder<String, HomeWork> find = new Finder<>(HomeWork.class);
}
