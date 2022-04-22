package dtos;

import enums.Size;
import enums.Status;
import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * A DTO class to transfer Task updates data
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskUpdateDto {
    private UUID id;
    private Status status;
    private String type;

    // Homework fields
    private String course;
    private Date dueDate;
    private String details;

    // Chore fields
    private String description;
    private Size size;
}
