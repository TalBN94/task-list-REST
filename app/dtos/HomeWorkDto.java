package dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * A DTO class to transfer HomeWork task type data
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeWorkDto extends TaskDto{
    private String course;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;
    private String details;

    public HomeWorkDto(UUID id, UUID ownerId, Status status, String course, Date dueDate, String details) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.course = course;
        this.dueDate = dueDate;
        this.details = details;
    }
}
