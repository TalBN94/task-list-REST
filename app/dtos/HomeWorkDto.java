package dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeWorkDto extends TaskDto{
    private String course;
    private Date dueDate;
    private String details;
    private final String type = Constants.HOMEWORK;
}
