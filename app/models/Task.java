package models;

import enums.Status;
import io.ebean.Model;
import lombok.Getter;
import lombok.Setter;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class Task extends Model {
    @Id
    @Column
    protected UUID id;

    @Column(nullable = false)
    @ManyToOne
    protected String ownerId;

    @Column(nullable = false)
    @Constraints.Required
    protected Status status;
}
