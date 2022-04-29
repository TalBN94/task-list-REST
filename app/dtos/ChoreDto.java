package dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import enums.Size;
import enums.Status;
import enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

import java.util.UUID;

/**
 * A DTO class to transfer Chore task type data
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChoreDto extends TaskDto{
    private String description;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private Size size;

    public ChoreDto(UUID id, UUID ownerId, Status status, String description, Size size) {
        super(id, TaskType.Chore, ownerId, status);
        this.description = description;
        this.size = size;
    }
}
