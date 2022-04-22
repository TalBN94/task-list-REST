package dtos;

import enums.Status;
import lombok.*;

import java.util.UUID;

/**
 * An abstract DTO class which should be extended by task types DTOs
 * */
@Getter
@Setter
public abstract class TaskDto {
    protected UUID id;
    protected UUID ownerId;
    protected Status status;
    protected String type;
}
