package dtos;

import enums.Status;
import lombok.*;

import java.util.UUID;

/**
 * A DTO class to transfer task data, which should be extended by all task types
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDto {
    protected UUID id;
    protected UUID ownerId;
    protected Status status;
    protected String type;
}
