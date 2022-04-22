package dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeWorkDto extends TaskDto{
    private String course;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;
    private String details;

    public HomeWorkDto(UUID id, UUID ownerId, Status status, String type, String course, Date dueDate, String details) {
        super(id, ownerId, status, type);
        this.course = course;
        this.dueDate = dueDate;
        this.details = details;
    }
}
