package dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import enums.Status;
import enums.TaskType;
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
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    protected TaskType type;
    protected UUID ownerId;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    protected Status status;
}
