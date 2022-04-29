package dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import enums.Size;
import enums.Status;
import enums.TaskType;
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
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private Status status;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private TaskType type;

    // Homework fields
    private String course;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;
    private String details;

    // Chore fields
    private String description;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private Size size;
}
