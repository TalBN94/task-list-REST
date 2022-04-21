package models;

import enums.Status;
import io.ebean.Model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class Task extends Model {
    @Id
    @Column
    protected UUID id;

    @Column(nullable = false)
    @ManyToOne(targetEntity = Person.class)
    protected UUID ownerId;

    @Column(nullable = false)
    protected Status status;
}
