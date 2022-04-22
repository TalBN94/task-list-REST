package models;

import enums.Status;
import io.ebean.Finder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

/**
 * An entity class for Homework
 * */
@Entity
@NoArgsConstructor
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

    public HomeWork(UUID id, UUID ownerId, Status status, String course, Date dueDate, String details) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.course = course;
        this.dueDate = dueDate;
        this.details = details;
    }
}
