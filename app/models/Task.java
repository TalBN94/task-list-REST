package models;

import enums.Status;
import io.ebean.Model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * An abstract class to be extended in task types entities
 * */
@MappedSuperclass
@Getter
@Setter
public abstract class Task extends Model {
    @Id
    @Column
    protected UUID id;

    @Column(nullable = false)
    protected UUID ownerId;

    @Column(nullable = false)
    protected Status status;
}
