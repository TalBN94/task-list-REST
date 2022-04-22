package dtos;

import enums.Size;
import enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChoreDto extends TaskDto{
    private String description;
    private Size size;

    public ChoreDto(UUID id, UUID ownerId, Status status, String type, String description, Size size) {
        super(id, ownerId, status, type);
        this.description = description;
        this.size = size;
    }
}
