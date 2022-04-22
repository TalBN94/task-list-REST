package dtos;

import enums.Status;
import lombok.*;

import java.util.UUID;

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
