package dtos;

import enums.Size;
import enums.Status;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDto {
    private UUID id;
    private UUID ownerId;
    private Status status;
    private String type;

    // Chore fields
    private String description;
    private Size size;

    // Homework fields
    private String course;
    private Date dueDate;
    private String details;


}
