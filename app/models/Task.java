package models;

import enums.Size;
import enums.Status;
import enums.TaskType;
import io.ebean.Finder;
import io.ebean.Model;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * An abstract class to be extended in task types entities
 * */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task extends Model {
    @Id
    @Column
    protected UUID id;

    @JoinColumn(name="ownerId", nullable = false)
    @ManyToOne
    protected Person owner;

    @Column(nullable = false)
    protected Status status;

    @Column(nullable = false)
    private TaskType type;

    // homework fields
    @Column
    private String course;

    @Column
    private Date dueDate;

    @Column
    private String details;

    // chore fields
    @Column
    private String description;

    @Column
    private Size size;

    public static Finder<UUID, Task> find = new Finder<>(Task.class);
}
