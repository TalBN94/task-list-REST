package models;

import enums.Size;
import enums.Status;
import io.ebean.Finder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Chore extends Task{

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Size size;

    public static Finder<String, Chore> find = new Finder<>(Chore.class);

    public Chore(UUID id, UUID ownerId, Status status, String description, Size size) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.description = description;
        this.size = size;
    }
}
