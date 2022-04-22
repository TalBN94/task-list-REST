package dtos;

import enums.Size;
import enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Size size;

    public ChoreDto(UUID id, UUID ownerId, Status status, String description, Size size) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.description = description;
        this.size = size;
    }
}
